package com.ironhack.midterm.service.impl;

import com.ironhack.midterm.controller.dto.MoneyDTO;
import com.ironhack.midterm.controller.dto.transactions.ThirdPartyTransactionDTO;
import com.ironhack.midterm.controller.dto.accounts.ThirdPartyAccountDTO;
import com.ironhack.midterm.dao.Constants;
import com.ironhack.midterm.dao.Money;
import com.ironhack.midterm.dao.transactions.ThirdPartyTransaction;
import com.ironhack.midterm.dao.transactions.Transaction;
import com.ironhack.midterm.dao.accounts.Account;
import com.ironhack.midterm.dao.accounts.accountsubclasses.CheckingAccount;
import com.ironhack.midterm.dao.accounts.accountsubclasses.SavingsAccount;
import com.ironhack.midterm.dao.accounts.accountsubclasses.StudentCheckingAccount;
import com.ironhack.midterm.dao.accounts.accountsubclasses.ThirdPartyAccount;
import com.ironhack.midterm.enums.Status;
import com.ironhack.midterm.repository.transactions.TransactionRepository;
import com.ironhack.midterm.repository.accounts.*;
import com.ironhack.midterm.repository.users.ThirdPartyRepository;
import com.ironhack.midterm.repository.users.UserRepository;
import com.ironhack.midterm.service.interfaces.IThirdPartyTransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ThirdPartyTransactionService implements IThirdPartyTransactionService {

    @Autowired
    private ThirdPartyAccountRepository thirdPartyAccountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CheckingAccountRepository checkingAccountRepository;

    @Autowired
    private SavingsAccountRepository savingsAccountRepository;

    @Autowired
    private StudentCheckingAccountRepository studentCheckingAccountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    @Autowired
    private ModelMapper modelMapper;

    public MoneyDTO retrieveThirdPartyBalance(long accountid, String username) {
        Optional<ThirdPartyAccount> foundAccount = thirdPartyAccountRepository.findById(accountid);
        if (!foundAccount.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"There is no third party checking account with id " + accountid + ". Please try again.");
        }
        String sUsername = null;
        if (foundAccount.get().getSecondaryOwner() != null) {
            sUsername = foundAccount.get().getSecondaryOwner().getUsername();
            if (!foundAccount.get().getPrimaryOwner().getUsername().equals(username) && !sUsername.equals(username)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You do not have permission to access this account.");
            }
        }
        else {
            if (!foundAccount.get().getPrimaryOwner().getUsername().equals(username)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access this account.");
            }
        }
            return convertToMoneyDto(foundAccount.get().getBalance());
    }

    public void failedThirdPartyTransaction(ThirdPartyAccount account, ThirdPartyTransaction transaction) {
        account.getPaymentTransactions().add(transaction);
        thirdPartyAccountRepository.save(account);
        transaction.setSuccessful(false);
        transactionRepository.save(transaction);
    }

    public void successfulThirdPartyTransaction(ThirdPartyAccount transferAccount, Account receivingAccount, ThirdPartyTransaction transaction) {
        transferAccount.getPaymentTransactions().add(transaction);
        receivingAccount.getReceivingTransactions().add(transaction);
        thirdPartyAccountRepository.save(transferAccount);
        accountRepository.save(receivingAccount);
        transaction.setSuccessful(true);
        transactionRepository.save(transaction);
    }

    public void transferFundsThirdParty(String hashedKey, ThirdPartyTransactionDTO transactionDTO) {
        ThirdPartyTransaction newTransaction = convertToThirdPartyTransaction(transactionDTO);
        Optional<ThirdPartyAccount> transferAccount = thirdPartyAccountRepository.findById(newTransaction.getThirdPartyTransferAccount().getId());
        if (!transferAccount.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Third party account does not exist.");
        }
        if (transferAccount.get().getSecondaryOwner() != null) {
            if (!transferAccount.get().getPrimaryOwner().getHashedKey().equals(hashedKey) && !transferAccount.get().getSecondaryOwner().getHashedKey().equals(hashedKey)) {
                failedThirdPartyTransaction(newTransaction.getThirdPartyTransferAccount(), newTransaction);
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access this account.");
            }
        }
        if (transferAccount.get().getSecondaryOwner() == null) {
            if (!transferAccount.get().getPrimaryOwner().getHashedKey().equals(hashedKey)) {
                failedThirdPartyTransaction(newTransaction.getThirdPartyTransferAccount(), newTransaction);
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access this account.");
            }
        }
        if (newTransaction.getThirdPartyTransferAccount().getStatus().equals(Status.FROZEN) || newTransaction.getReceivingAccount().getStatus().equals(Status.FROZEN)) {
            failedThirdPartyTransaction(newTransaction.getThirdPartyTransferAccount(),newTransaction);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Account is frozen, and no transactions can be made.");
        }
        if (!newTransaction.getSecretKey().equals(findSecretKey(newTransaction.getReceivingAccount()))) {
            failedThirdPartyTransaction(newTransaction.getThirdPartyTransferAccount(),newTransaction);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Secret key does not match, access has been denied.");
        }
        Boolean penaltyCheck = applyPenaltyFee(newTransaction.getThirdPartyTransferAccount(),newTransaction);
        Boolean fraudFound = fraudFound(newTransaction.getThirdPartyTransferAccount(),newTransaction);
        if (!penaltyCheck && !fraudFound) {
            newTransaction.getThirdPartyTransferAccount().getBalance().decreaseAmount(newTransaction.getTransactionAmount());
            newTransaction.getReceivingAccount().getBalance().increaseAmount(newTransaction.getTransactionAmount());
            successfulThirdPartyTransaction(newTransaction.getThirdPartyTransferAccount(),newTransaction.getReceivingAccount(),newTransaction);
        }
        else if (fraudFound) {
            newTransaction.getThirdPartyTransferAccount().setStatus(Status.FROZEN);
            failedThirdPartyTransaction(newTransaction.getThirdPartyTransferAccount(),newTransaction);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Fraud has been identified. Your account has now been frozen.");
        }
        else {
            newTransaction.getThirdPartyTransferAccount().getBalance().decreaseAmount(Constants.PENALTY_FEE);
            failedThirdPartyTransaction(newTransaction.getThirdPartyTransferAccount(),newTransaction);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Insufficient funds available.");
        }
    }

    public Boolean applyPenaltyFee(ThirdPartyAccount transferAccount, Transaction transaction) {
        BigDecimal accountBalance = transferAccount.getBalance().getAmount();
        BigDecimal transactionTotal = transaction.getTransactionAmount();
        if (accountBalance.subtract(transactionTotal).signum() == -1) {
            return true;
        }
        return false;
    }

    public Boolean fraudFound(ThirdPartyAccount account, Transaction transaction) {
        LocalDateTime now = new Timestamp(System.currentTimeMillis()).toLocalDateTime();
        if (account.getPaymentTransactions().size() <= 2) {
            return false;
        }
        LocalDateTime secondLastTransaction = account.getPaymentTransactions().get(account.getPaymentTransactions().size() -2).getTransactionDate();
        if (now.getDayOfYear() == secondLastTransaction.getDayOfYear() && now.getHour() == secondLastTransaction.getHour() &&
                now.getMinute() == secondLastTransaction.getMinute() && now.getSecond() == secondLastTransaction.getSecond()) {
            return true;
        }
        if (account.getCreationDate().compareTo(now.toLocalDate().minusDays(1)) < 0) {
            return false;
        }
        BigDecimal highestDailyTotalMarker = findHighestDailyTotal(account).multiply(BigDecimal.valueOf(1.5));
        BigDecimal transactionsIn24hrs = transactionTotalInLastDay(account);
        if (transactionsIn24hrs.compareTo(highestDailyTotalMarker) > 0) {
            return true;
        }
        if (!account.getPaymentTransactions().get(account.getPaymentTransactions().size() - 1).getSuccessful() &&
                !account.getPaymentTransactions().get(account.getPaymentTransactions().size() - 2).getSuccessful()) {
            return true;
        }
        return false;
    }

    public BigDecimal findHighestDailyTotal(ThirdPartyAccount account) {
        if (account.getPaymentTransactions().size() < 1) {
            return BigDecimal.valueOf(0);
        }
        BigDecimal maxTransaction = new BigDecimal(0);
        BigDecimal workingTotal = new BigDecimal(0);
        LocalDate checkedTransactionDate = account.getPaymentTransactions().get(0).getTransactionDate().toLocalDate();
        for (int i = 0; i < account.getPaymentTransactions().size(); i++) {
            if (account.getPaymentTransactions().get(i).getTransactionDate().toLocalDate().equals(checkedTransactionDate)) {
                workingTotal.add(account.getPaymentTransactions().get(i).getTransactionAmount());
            }
            else {
                if (workingTotal.compareTo(maxTransaction) > 0) {
                    maxTransaction = workingTotal;
                }
                workingTotal = BigDecimal.valueOf(0);
                checkedTransactionDate = account.getPaymentTransactions().get(i).getTransactionDate().toLocalDate();
            }
        }
        return maxTransaction;
    }

    public BigDecimal transactionTotalInLastDay(ThirdPartyAccount account) {
        LocalDateTime timeNow = LocalDateTime.now();
        LocalDateTime timeYesterday = timeNow.minusDays(1);
        BigDecimal transactionTotal = BigDecimal.valueOf(0);
        for (int i = 0; i < account.getPaymentTransactions().size(); i++) {
            if (account.getPaymentTransactions().get(i).getTransactionDate().isBefore(timeNow) &&
                    account.getPaymentTransactions().get(i).getTransactionDate().isAfter(timeYesterday)) {
                transactionTotal.add(account.getPaymentTransactions().get(i).getTransactionAmount());
            }
        }
        return transactionTotal;
    }

    public ThirdPartyTransaction convertToThirdPartyTransaction(ThirdPartyTransactionDTO transactionDTO) {
        Optional<ThirdPartyAccount> transferAccount = thirdPartyAccountRepository.findById(transactionDTO.getTransferAccountId());
        Optional<Account> receivingAccount = accountRepository.findById(transactionDTO.getReceivingAccountId());
        if (!transferAccount.isPresent() && !receivingAccount.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Account not found.");
        }
        ThirdPartyTransaction transaction = new ThirdPartyTransaction(transactionDTO.getTransactionAmount(),transferAccount.get(),receivingAccount.get(),transactionDTO.getReceivingSecretKey());
        return transaction;
    }

    public String findSecretKey(Account account) {
        Optional<CheckingAccount> accountCheck = checkingAccountRepository.findById(account.getId());
        if (accountCheck.isPresent()) {
            return accountCheck.get().getSecretKey();
        }
        Optional<StudentCheckingAccount> accountStudent = studentCheckingAccountRepository.findById(account.getId());
        if (accountStudent.isPresent()) {
            return accountStudent.get().getSecretKey();
        }
        Optional<SavingsAccount> accountSavings = savingsAccountRepository.findById(account.getId());
        if (accountSavings.isPresent()) {
            return accountSavings.get().getSecretKey();
        }
        return null;
    }

    public MoneyDTO convertToMoneyDto(Money money) {
        MoneyDTO moneyDTO = modelMapper.map(money, MoneyDTO.class);
        return moneyDTO;
    }

    public ThirdPartyAccountDTO convertToThirdPartyAccountDto(ThirdPartyAccount account) {
        ThirdPartyAccountDTO thirdPartyAccountDTO = modelMapper.map(account, ThirdPartyAccountDTO.class);
        return thirdPartyAccountDTO;
    }
}

package com.ironhack.midterm.service.impl;

import com.ironhack.midterm.controller.dto.MoneyDTO;
import com.ironhack.midterm.controller.dto.ThirdPartyTransactionDTO;
import com.ironhack.midterm.controller.dto.TransactionDTO;
import com.ironhack.midterm.dao.Constants;
import com.ironhack.midterm.dao.Money;
import com.ironhack.midterm.dao.Transaction;
import com.ironhack.midterm.dao.accounts.Account;
import com.ironhack.midterm.dao.accounts.accountsubclasses.*;
import com.ironhack.midterm.dao.users.usersubclasses.ThirdParty;
import com.ironhack.midterm.enums.Status;
import com.ironhack.midterm.exceptions.BalanceOutOfBoundsException;
import com.ironhack.midterm.repository.TransactionRepository;
import com.ironhack.midterm.repository.accounts.*;
import com.ironhack.midterm.repository.users.ThirdPartyRepository;
import com.ironhack.midterm.repository.users.UserRepository;
import com.ironhack.midterm.service.interfaces.ITransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MONTHS;

@Service
public class TransactionService implements ITransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CheckingAccountRepository checkingAccountRepository;

    @Autowired
    private StudentCheckingAccountRepository studentCheckingAccountRepository;

    @Autowired
    private CreditCardAccountRepository creditCardAccountRepository;

    @Autowired
    private SavingsAccountRepository savingsAccountRepository;

    @Autowired
    private ThirdPartyAccountRepository thirdPartyAccountRepository;

    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public MoneyDTO retrieveCheckingBalance(long accountid, String username) {
        Optional<CheckingAccount> foundAccount = checkingAccountRepository.findById(accountid);
        if (!foundAccount.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"There is no checking account with id " + accountid + ". Please try again.");
        }
        if (!foundAccount.get().getPrimaryOwner().getUsername().equals(username) && !foundAccount.get().getSecondaryOwner().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You do not have permission to access this account.");
        }
        BigDecimal newBalance = applyMonthlyMaintenance(foundAccount.get());
        foundAccount.get().setBalance(new Money(newBalance,Currency.getInstance("GBP")));
        foundAccount.get().setMonthlyMaintenanceFeeLastApplied(LocalDate.now());
        checkingAccountRepository.save(foundAccount.get());
        return convertToMoneyDto(foundAccount.get().getBalance());
    }

    public MoneyDTO retrieveStudentCheckingBalance(long accountid, String username) {
        Optional<StudentCheckingAccount> foundAccount = studentCheckingAccountRepository.findById(accountid);
        if (!foundAccount.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"There is no student checking account with id " + accountid + ". Please try again.");
        }
        if (!foundAccount.get().getPrimaryOwner().getUsername().equals(username) && !foundAccount.get().getSecondaryOwner().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You do not have permission to access this account.");
        }
        else {
            return convertToMoneyDto(foundAccount.get().getBalance());
        }
    }

    public MoneyDTO retrieveSavingsBalance(long accountid, String username) {
        Optional<SavingsAccount> foundAccount = savingsAccountRepository.findById(accountid);
        if (!foundAccount.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"There is no savings account with id " + accountid + ". Please try again.");
        }
        if (!foundAccount.get().getPrimaryOwner().getUsername().equals(username) && !foundAccount.get().getSecondaryOwner().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You do not have permission to access this account.");
        }
        else {
            BigDecimal newBalance = applyYearlyInterest(foundAccount.get());
            foundAccount.get().setBalance(new Money(newBalance,Currency.getInstance("GBP")));
            foundAccount.get().setSavingsInterestLastApplied(LocalDate.now());
            savingsAccountRepository.save(foundAccount.get());
            return convertToMoneyDto(foundAccount.get().getBalance());
        }
    }

    public MoneyDTO retrieveCreditCardBalance(long accountid, String username) {
        Optional<CreditCardAccount> foundAccount = creditCardAccountRepository.findById(accountid);
        if (!foundAccount.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"There is no credit card account with id " + accountid + ". Please try again.");
        }
        if (!foundAccount.get().getPrimaryOwner().getUsername().equals(username) && !foundAccount.get().getSecondaryOwner().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You do not have permission to access this account.");
        }
        else {
            BigDecimal newBalance = applyMonthlyInterest(foundAccount.get());
            foundAccount.get().setBalance(new Money(newBalance,Currency.getInstance("GBP")));
            foundAccount.get().setCreditCardInterestLastApplied(LocalDate.now());
            creditCardAccountRepository.save(foundAccount.get());
            return convertToMoneyDto(foundAccount.get().getBalance());
        }
    }

    public MoneyDTO retrieveThirdPartyBalance(long accountid, String username) {
        Optional<ThirdPartyAccount> foundAccount = thirdPartyAccountRepository.findById(accountid);
        if (!foundAccount.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"There is no third party checking account with id " + accountid + ". Please try again.");
        }
        if (!foundAccount.get().getPrimaryOwner().getUsername().equals(username) && !foundAccount.get().getSecondaryOwner().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You do not have permission to access this account.");
        }
        else {
            return convertToMoneyDto(foundAccount.get().getBalance());
        }
    }

    private MoneyDTO convertToMoneyDto(Money money) {
        MoneyDTO moneyDTO = modelMapper.map(money, MoneyDTO.class);
        return moneyDTO;
    }

    private Money convertToMoneyEntity(MoneyDTO money) {
        Money newMoney = modelMapper.map(money, Money.class);
        return newMoney;
    }

    public BigDecimal applyMonthlyMaintenance(CheckingAccount account) {
        BigDecimal monthsBetween = null;
        if (account.getMonthlyMaintenanceFeeLastApplied() == null) {
            monthsBetween = BigDecimal.valueOf(MONTHS.between (
                    account.getCreationDate(),
                    LocalDate.now()
            )).setScale(0, RoundingMode.DOWN);
        }
        else {
            monthsBetween = BigDecimal.valueOf(MONTHS.between (
                    account.getMonthlyMaintenanceFeeLastApplied(),
                    LocalDate.now()
            )).setScale(0, RoundingMode.DOWN);
        }
        if (monthsBetween.compareTo(BigDecimal.valueOf(1)) >= 0) {
            BigDecimal newBalance = account.getBalance().getAmount().add(Constants.CHECKING_MAINTENANCE_FEE.multiply(monthsBetween));
            return newBalance;
        }
        return account.getBalance().getAmount();
    }

    public BigDecimal applyYearlyInterest(SavingsAccount account) {
        BigDecimal yearsBetween = null;
        if (account.getSavingsInterestLastApplied() == null){
            yearsBetween = new BigDecimal(ChronoUnit.YEARS.between(
                    account.getCreationDate(),
                    LocalDate.now()
            )).setScale(0, RoundingMode.DOWN);
        }
        else {
            yearsBetween = new BigDecimal(ChronoUnit.YEARS.between(
                    account.getSavingsInterestLastApplied(),
                    LocalDate.now()
            )).setScale(0, RoundingMode.DOWN);
        }
        if (yearsBetween.compareTo(BigDecimal.valueOf(1)) >= 0) {
            BigDecimal newBalance = compoundSavingsInterest(yearsBetween,account);
            return newBalance;
        }
        return account.getBalance().getAmount();
    }

    public BigDecimal applyMonthlyInterest(CreditCardAccount account) {
        BigDecimal monthsBetween = null;
        if (account.getCreditCardInterestLastApplied() == null) {
            monthsBetween = BigDecimal.valueOf(ChronoUnit.MONTHS.between(
                    account.getCreationDate(),
                    LocalDate.now()
            ));
        }
        else {
            monthsBetween = BigDecimal.valueOf(ChronoUnit.MONTHS.between(
                    account.getCreditCardInterestLastApplied(),
                    LocalDate.now()
            ));
        }
        if (monthsBetween.compareTo(BigDecimal.valueOf(1)) >= 0) {
            BigDecimal newBalance = compoundCreditCardInterest(monthsBetween,account);
            return newBalance;
        }
        return account.getBalance().getAmount();
    }

    public BigDecimal compoundSavingsInterest(BigDecimal timeBetween, SavingsAccount account) {
        BigDecimal newBalance = account.getBalance().getAmount();
        BigDecimal tempValue = null;
        for (int i = 0; i < timeBetween.intValue(); i++) {
            tempValue = newBalance.multiply(account.getInterestRate().getAmount());
            newBalance = newBalance.add(tempValue);
        }
        return newBalance;
    }

    public BigDecimal compoundCreditCardInterest(BigDecimal timeBetween, CreditCardAccount account) {
        BigDecimal newBalance = account.getBalance().getAmount();
        BigDecimal tempValue = null;
        BigDecimal monthlyInterest = account.getInterestRate().getAmount().divide(BigDecimal.valueOf(12),3,RoundingMode.HALF_UP);
        for (int i = 0; i < timeBetween.intValue(); i++) {
            tempValue = newBalance.multiply(monthlyInterest);
            newBalance = newBalance.add(tempValue);
        }
        return newBalance;
    }

    public Boolean applyPenaltyFee(Account transferAccount, Transaction transaction) {
        BigDecimal accountBalance = transferAccount.getBalance().getAmount();
        BigDecimal transactionTotal = transaction.getTransactionAmount();
        if (accountBalance.subtract(transactionTotal).signum() == -1) {
            return true;
        }
        return false;
    }

    public Boolean fraudFound(Account account, Transaction transaction) {
        LocalDateTime now = new Timestamp(System.currentTimeMillis()).toLocalDateTime();
        if (account.getPaymentTransactions().size() < 2) {
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

    public BigDecimal findHighestDailyTotal(Account account) {
        System.out.println("Before size check");
        if (account.getPaymentTransactions().size() < 1) {
            return BigDecimal.valueOf(0);
        }
        BigDecimal maxTransaction = new BigDecimal(0);
        BigDecimal workingTotal = new BigDecimal(0);
        LocalDate checkedTransactionDate = account.getPaymentTransactions().get(0).getTransactionDate().toLocalDate();
        System.out.println("Before Loop");
        for (int i = 0; i < account.getPaymentTransactions().size(); i++) {
            System.out.println("Test " + i);
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

    public BigDecimal transactionTotalInLastDay(Account account) {
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

    public void failedTransaction(Account account, Transaction transaction) {
        account.getPaymentTransactions().add(transaction);
        accountRepository.save(account);
        transaction.setSuccessful(false);
        transactionRepository.save(transaction);
    }

    public void successfulTransaction(Account transferAccount, Account receivingAccount, Transaction transaction) {
        transferAccount.getPaymentTransactions().add(transaction);
        receivingAccount.getReceivingTransactions().add(transaction);
        accountRepository.save(transferAccount);
        accountRepository.save(receivingAccount);
        transaction.setSuccessful(true);
        transactionRepository.save(transaction);
    }

    public void transferFunds(TransactionDTO transactionDTO) throws BalanceOutOfBoundsException {
        Transaction newTransaction = convertToTransaction(transactionDTO);
        if (newTransaction.getTransferAccount().getStatus().equals(Status.FROZEN) || newTransaction.getReceivingAccount().getStatus().equals(Status.FROZEN)) {
            failedTransaction(newTransaction.getTransferAccount(),newTransaction);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Account is frozen, and no transactions can be made.");
        }
        Boolean penaltyCheck = applyPenaltyFee(newTransaction.getTransferAccount(),newTransaction);
        Boolean fraudFound = fraudFound(newTransaction.getTransferAccount(),newTransaction);
        if (!penaltyCheck && !fraudFound) {
            newTransaction.getTransferAccount().getBalance().decreaseAmount(newTransaction.getTransactionAmount());
            newTransaction.getReceivingAccount().getBalance().increaseAmount(newTransaction.getTransactionAmount());
            successfulTransaction(newTransaction.getTransferAccount(),newTransaction.getReceivingAccount(),newTransaction);
        }
        else if (fraudFound) {
            newTransaction.getTransferAccount().setStatus(Status.FROZEN);
            failedTransaction(newTransaction.getTransferAccount(),newTransaction);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Fraud has been identified. Your account has now been frozen.");
        }
        else {
            newTransaction.getTransferAccount().getBalance().decreaseAmount(Constants.PENALTY_FEE);
            failedTransaction(newTransaction.getTransferAccount(),newTransaction);
            throw new BalanceOutOfBoundsException("Insufficient funds available.");
        }
    }

    public void transferFundsAccHolder(String username, TransactionDTO transactionDTO) throws BalanceOutOfBoundsException {
        Transaction newTransaction = convertToTransaction(transactionDTO);
        if (!newTransaction.getTransferAccount().getPrimaryOwner().getUsername().equals(username) && !newTransaction.getTransferAccount().getSecondaryOwner().getUsername().equals(username)) {
            failedTransaction(newTransaction.getTransferAccount(),newTransaction);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You do not have permission to access this account.");
        }
        if (newTransaction.getTransferAccount().getStatus().equals(Status.FROZEN) || newTransaction.getReceivingAccount().getStatus().equals(Status.FROZEN)) {
            failedTransaction(newTransaction.getTransferAccount(),newTransaction);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Account is frozen, and no transactions can be made.");
        }
        Boolean penaltyCheck = applyPenaltyFee(newTransaction.getTransferAccount(),newTransaction);
        Boolean fraudFound = fraudFound(newTransaction.getTransferAccount(),newTransaction);
        if (!penaltyCheck && !fraudFound) {
            newTransaction.getTransferAccount().getBalance().decreaseAmount(newTransaction.getTransactionAmount());
            newTransaction.getReceivingAccount().getBalance().increaseAmount(newTransaction.getTransactionAmount());
            successfulTransaction(newTransaction.getTransferAccount(),newTransaction.getReceivingAccount(),newTransaction);
        }
        else if (fraudFound) {
            newTransaction.getTransferAccount().setStatus(Status.FROZEN);
            failedTransaction(newTransaction.getTransferAccount(),newTransaction);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Fraud has been identified. Your account has now been frozen.");
        }
        else {
            newTransaction.getTransferAccount().getBalance().decreaseAmount(Constants.PENALTY_FEE);
            failedTransaction(newTransaction.getTransferAccount(),newTransaction);
            throw new BalanceOutOfBoundsException("Insufficient funds available.");
        }
    }

    public void transferFundsThirdParty(String username, String hashedKey, TransactionDTO transactionDTO) throws BalanceOutOfBoundsException {
        Transaction newTransaction = convertToTransaction(transactionDTO);
        Optional<ThirdParty> primaryOwner = thirdPartyRepository.findById(newTransaction.getTransferAccount().getPrimaryOwner().getId());
        Optional<ThirdParty> secondaryOwner = thirdPartyRepository.findById(newTransaction.getTransferAccount().getSecondaryOwner().getId());
        if ((!primaryOwner.get().getUsername().equals(username) && !secondaryOwner.get().getUsername().equals(username)) ||
                (!primaryOwner.get().getHashedKey().equals(hashedKey) && !secondaryOwner.get().getHashedKey().equals(hashedKey))) {
            failedTransaction(newTransaction.getTransferAccount(),newTransaction);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You do not have permission to access this account.");
        }
        if (newTransaction.getTransferAccount().getStatus().equals(Status.FROZEN) || newTransaction.getReceivingAccount().getStatus().equals(Status.FROZEN)) {
            failedTransaction(newTransaction.getTransferAccount(),newTransaction);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Account is frozen, and no transactions can be made.");
        }
        if (!hashedKey.equals(findSecretKey(newTransaction.getReceivingAccount()))) {
            failedTransaction(newTransaction.getTransferAccount(),newTransaction);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Secret key does not match, access has been denied.");
        }
        Boolean penaltyCheck = applyPenaltyFee(newTransaction.getTransferAccount(),newTransaction);
        Boolean fraudFound = fraudFound(newTransaction.getTransferAccount(),newTransaction);
        if (!penaltyCheck && !fraudFound) {
            newTransaction.getTransferAccount().getBalance().decreaseAmount(newTransaction.getTransactionAmount());
            newTransaction.getReceivingAccount().getBalance().increaseAmount(newTransaction.getTransactionAmount());
            successfulTransaction(newTransaction.getTransferAccount(),newTransaction.getReceivingAccount(),newTransaction);
        }
        else if (fraudFound) {
            newTransaction.getTransferAccount().setStatus(Status.FROZEN);
            failedTransaction(newTransaction.getTransferAccount(),newTransaction);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Fraud has been identified. Your account has now been frozen.");
        }
        else {
            newTransaction.getTransferAccount().getBalance().decreaseAmount(Constants.PENALTY_FEE);
            failedTransaction(newTransaction.getTransferAccount(),newTransaction);
            throw new BalanceOutOfBoundsException("Insufficient funds available.");
        }
    }

    public Transaction convertToTransaction(TransactionDTO transactionDTO) {
        Optional<Account> transferAccount = accountRepository.findById(transactionDTO.getTransferAccountId());
        Optional<Account> receivingAccount = accountRepository.findById(transactionDTO.getReceivingAccountId());
        if (!transferAccount.isPresent() && !receivingAccount.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Account not found.");
        }
        Transaction transaction = new Transaction(transactionDTO.getTransactionAmount(),transferAccount.get(),receivingAccount.get());
        return transaction;
    }

    public Transaction convertToTransaction(ThirdPartyTransactionDTO transactionDTO) {
        Optional<Account> transferAccount = accountRepository.findById(transactionDTO.getTransferAccountId());
        Optional<Account> receivingAccount = accountRepository.findById(transactionDTO.getReceivingAccountId());
        if (!transferAccount.isPresent() && !receivingAccount.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Account not found.");
        }
        Transaction transaction = new Transaction(transactionDTO.getTransactionAmount(),transferAccount.get(),receivingAccount.get());
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
}

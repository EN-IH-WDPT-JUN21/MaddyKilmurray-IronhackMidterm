package com.ironhack.midterm.service.impl;

import com.ironhack.midterm.controller.dto.MoneyDTO;
import com.ironhack.midterm.controller.dto.TransactionDTO;
import com.ironhack.midterm.dao.Constants;
import com.ironhack.midterm.dao.Money;
import com.ironhack.midterm.dao.Transaction;
import com.ironhack.midterm.dao.accounts.Account;
import com.ironhack.midterm.dao.accounts.accountsubclasses.*;
import com.ironhack.midterm.enums.Status;
import com.ironhack.midterm.exceptions.BalanceOutOfBoundsException;
import com.ironhack.midterm.repository.TransactionRepository;
import com.ironhack.midterm.repository.accounts.*;
import com.ironhack.midterm.repository.users.UserRepository;
import com.ironhack.midterm.service.interfaces.ITransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Currency;
import java.util.Optional;

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

    public Boolean penaltyFeeCheck(Account transferAccount, Transaction transaction) {
        BigDecimal newAccountTotal = transferAccount.getBalance().getAmount().subtract(transaction.getTransactionAmount());
        if (newAccountTotal.compareTo(BigDecimal.valueOf(0)) < 0) {
            transferAccount.getBalance().decreaseAmount(Constants.PENALTY_FEE);
            return false;
        }
        else {
            transferAccount.setBalance(new Money(newAccountTotal));
            return true;
        }
    }

//    public void fraudCheck(Account account, Transactions transaction) {
//        List<Transactions> transactionsList = new ArrayList<>();
//        for (int i = 0; i < account.getTransactionRecord().size(); i++) {
//            transactionsList.add(account.getTransactionRecord().get(i));
//        }
//
//
//    }

    public void transferFunds(TransactionDTO transactionDTO) throws BalanceOutOfBoundsException {
        Transaction newTransaction = convertToTransaction(transactionDTO);
        if (newTransaction.getTransferAccount().getStatus().equals(Status.FROZEN) || newTransaction.getReceivingAccount().getStatus().equals(Status.FROZEN)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Account is frozen, and no transactions can be made.");
        }

        Boolean penaltyCheck = penaltyFeeCheck(newTransaction.getTransferAccount(),newTransaction);
        if (penaltyCheck == true) {
            newTransaction.getReceivingAccount().getBalance().increaseAmount(newTransaction.getTransactionAmount());
            accountRepository.save(newTransaction.getTransferAccount());
            accountRepository.save(newTransaction.getReceivingAccount());
        }
        else {
            accountRepository.save(newTransaction.getTransferAccount());
            throw new BalanceOutOfBoundsException("Insufficient funds available.");
        }
    }

    private Transaction convertToTransaction(TransactionDTO transactionDTO) {
        Optional<Account> transferAccount = accountRepository.findById(transactionDTO.getTransferAccountId());
        Optional<Account> receivingAccount = accountRepository.findById(transactionDTO.getReceivingAccountId());
        if (!transferAccount.isPresent() || !receivingAccount.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Account not found.");
        }
        Transaction transaction = new Transaction(transactionDTO.getTransactionAmount(),transferAccount.get(),receivingAccount.get());
        return transaction;
    }
}

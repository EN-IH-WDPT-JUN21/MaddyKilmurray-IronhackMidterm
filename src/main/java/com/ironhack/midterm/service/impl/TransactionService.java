package com.ironhack.midterm.service.impl;

import com.ironhack.midterm.controller.dto.MoneyDTO;
import com.ironhack.midterm.controller.dto.users.UserDTO;
import com.ironhack.midterm.dao.Constants;
import com.ironhack.midterm.dao.Money;
import com.ironhack.midterm.dao.accounts.Account;
import com.ironhack.midterm.dao.accounts.accountsubclasses.*;
import com.ironhack.midterm.dao.users.User;
import com.ironhack.midterm.repository.accounts.*;
import com.ironhack.midterm.repository.users.UserRepository;
import com.ironhack.midterm.service.interfaces.ITransactionService;
import org.apache.tomcat.jni.Local;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
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
            foundAccount.get().setInterestLastApplied(LocalDate.now());
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
            foundAccount.get().setInterestRateLastApplied(LocalDate.now());
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
        if (account.getInterestLastApplied() == null){
            yearsBetween = new BigDecimal(ChronoUnit.YEARS.between(
                    account.getCreationDate(),
                    LocalDate.now()
            )).setScale(0, RoundingMode.DOWN);
        }
        else {
            yearsBetween = new BigDecimal(ChronoUnit.YEARS.between(
                    account.getInterestLastApplied(),
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
        if (account.getInterestRateLastApplied() == null) {
            monthsBetween = BigDecimal.valueOf(ChronoUnit.MONTHS.between(
                    account.getCreationDate(),
                    LocalDate.now()
            ));
        }
        else {
            monthsBetween = BigDecimal.valueOf(ChronoUnit.MONTHS.between(
                    account.getInterestRateLastApplied(),
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

    public void penaltyFeeCheck(Account account) {
        if (account.getBalance().getAmount().compareTo(BigDecimal.valueOf(0)) < 0) {
            BigDecimal penaltyFeeApplied = account.getBalance().getAmount().subtract(Constants.PENALTY_FEE);
            account.setBalance(new Money(penaltyFeeApplied));
        }
    }
}

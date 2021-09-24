package com.ironhack.midterm.service.impl;

import com.ironhack.midterm.controller.interfaces.ITransactionController;
import com.ironhack.midterm.dao.Money;
import com.ironhack.midterm.dao.accounts.Account;
import com.ironhack.midterm.dao.accounts.accountsubclasses.*;
import com.ironhack.midterm.repository.*;
import com.ironhack.midterm.service.interfaces.ITransactionService;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static com.ironhack.midterm.dao.accounts.accountsubclasses.CheckingAccount.applyMonthlyMaintenance;
import static com.ironhack.midterm.dao.accounts.accountsubclasses.CreditCardAccount.applyMonthlyInterest;
import static com.ironhack.midterm.dao.accounts.accountsubclasses.SavingsAccount.applyYearlyInterest;

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

    public Money retrieveCheckingBalance(long accountid, String username) {
        Optional<CheckingAccount> foundAccount = checkingAccountRepository.findById(accountid);
        if (!foundAccount.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"There is no checking account with id " + accountid + ". Please try again.");
        }
        if (foundAccount.get().getPrimaryOwner().getUsername().equals(username) || foundAccount.get().getSecondaryOwner().getUsername().equals(username)) {
            applyMonthlyMaintenance(foundAccount.get());
            checkingAccountRepository.save(foundAccount.get());
            return foundAccount.get().getBalance();
        }
        else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You do not have permission to access this account.");
        }

    }

    public Money retrieveStudentCheckingBalance(long accountid, String username) {
        Optional<StudentCheckingAccount> foundAccount = studentCheckingAccountRepository.findById(accountid);
        if (!foundAccount.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"There is no student checking account with id " + accountid + ". Please try again.");
        }
        if (foundAccount.get().getPrimaryOwner().getUsername().equals(username) || foundAccount.get().getSecondaryOwner().getUsername().equals(username)) {
            return foundAccount.get().getBalance();
        }
        else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You do not have permission to access this account.");
        }
    }

    public Money retrieveSavingsBalance(long accountid, String username) {
        Optional<SavingsAccount> foundAccount = savingsAccountRepository.findById(accountid);
        if (!foundAccount.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"There is no savings account with id " + accountid + ". Please try again.");
        }
        if (foundAccount.get().getPrimaryOwner().getUsername().equals(username) || foundAccount.get().getSecondaryOwner().getUsername().equals(username)) {
            applyYearlyInterest(foundAccount.get());
            savingsAccountRepository.save(foundAccount.get());
            return foundAccount.get().getBalance();
        }
        else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You do not have permission to access this account.");
        }
    }

    public Money retrieveCreditCardBalance(long accountid, String username) {
        Optional<CreditCardAccount> foundAccount = creditCardAccountRepository.findById(accountid);
        if (!foundAccount.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"There is no credit card account with id " + accountid + ". Please try again.");
        }
        if (foundAccount.get().getPrimaryOwner().getUsername().equals(username) || foundAccount.get().getSecondaryOwner().getUsername().equals(username)) {
            applyMonthlyInterest(foundAccount.get());
            creditCardAccountRepository.save(foundAccount.get());
            return foundAccount.get().getBalance();
        }
        else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You do not have permission to access this account.");
        }
    }

    public Money retrieveThirdPartyBalance(long accountid, String username) {
        Optional<ThirdPartyAccount> foundAccount = thirdPartyAccountRepository.findById(accountid);
        if (!foundAccount.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"There is no third party checking account with id " + accountid + ". Please try again.");
        }
        if (foundAccount.get().getPrimaryOwner().getUsername().equals(username) || foundAccount.get().getSecondaryOwner().getUsername().equals(username)) {
            return foundAccount.get().getBalance();
        }
        else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You do not have permission to access this account.");
        }
    }
}
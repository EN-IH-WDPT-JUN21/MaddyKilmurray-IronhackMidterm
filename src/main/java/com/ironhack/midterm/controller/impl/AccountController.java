package com.ironhack.midterm.controller.impl;

import com.ironhack.midterm.dao.accounts.Account;
import com.ironhack.midterm.repository.*;
import com.ironhack.midterm.service.interfaces.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AccountController {

    @Autowired
    private CheckingAccountRepository checkingAccountRepository;

    @Autowired
    private CreditCardAccountRepository creditCardAccountRepository;

    @Autowired
    private SavingsAccountRepository savingsAccountRepository;

    @Autowired
    private StudentCheckingAccountRepository studentCheckingAccountRepository;

    @Autowired
    private ThirdPartyAccountRepository thirdPartyAccountRepository;

    @Autowired
    private IAccountService accountService;

    @GetMapping("/accounts")
    @ResponseStatus(HttpStatus.OK)
    public List<Account> getAll() {
        List<Account> fullList = new ArrayList<>();

        fullList.addAll(checkingAccountRepository.findAll());
        fullList.addAll(studentCheckingAccountRepository.findAll());
        fullList.addAll(savingsAccountRepository.findAll());
        fullList.addAll(thirdPartyAccountRepository.findAll());
        return fullList;
    };

    @PostMapping("/accounts/new/{accountType}")
    @ResponseStatus(HttpStatus.CREATED)
    public Account createNew(@PathVariable(name = "accountType") String accountType, @RequestBody @Valid Account account) {
        return accountService.createNewAccount(accountType,account);
    }
}

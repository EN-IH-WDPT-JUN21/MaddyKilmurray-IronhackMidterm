package com.ironhack.midterm.controller.impl;

import com.ironhack.midterm.controller.dto.accounts.*;
import com.ironhack.midterm.dao.accounts.Account;
import com.ironhack.midterm.dao.accounts.accountsubclasses.CheckingAccount;
import com.ironhack.midterm.dao.accounts.accountsubclasses.CreditCardAccount;
import com.ironhack.midterm.dao.accounts.accountsubclasses.SavingsAccount;
import com.ironhack.midterm.dao.accounts.accountsubclasses.ThirdPartyAccount;
import com.ironhack.midterm.dao.users.User;
import com.ironhack.midterm.repository.accounts.AccountRepository;
import com.ironhack.midterm.repository.users.UserRepository;
import com.ironhack.midterm.service.interfaces.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IAccountService accountService;

    @GetMapping("/accounts")
    @ResponseStatus(HttpStatus.OK)
    public List<Account> findAll() {
        return accountRepository.findAll();
    };

    @GetMapping("/accounts/byid/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<User> findById(@PathVariable("id") Long id) {
        return userRepository.findById(id);
    }

    @PostMapping("/accounts/new/checking")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDTO createNewAccount(@RequestBody @Valid CheckingAccountDTO account) {
        return accountService.createNewCheckingAccount(account);
    }

    @PostMapping("/accounts/new/savings")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDTO createNewAccount(@RequestBody @Valid SavingsAccountDTO account) {
        return accountService.createNewSavingsAccount(account);
    }

    @PostMapping("/accounts/new/creditcard")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDTO createNewAccount(@RequestBody @Valid CreditCardAccountDTO account) {
        return accountService.createNewCreditCardAccount(account);
    }

    @PostMapping("/accounts/new/thirdparty")
    @ResponseStatus(HttpStatus.CREATED)
    public ThirdPartyAccountDTO createNewAccount(@RequestBody @Valid ThirdPartyAccountDTO account) {
        return accountService.createNewThirdPartyAccount(account);
    }


}

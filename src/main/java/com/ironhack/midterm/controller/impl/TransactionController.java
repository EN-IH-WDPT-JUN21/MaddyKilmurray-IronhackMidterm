package com.ironhack.midterm.controller.impl;

import com.ironhack.midterm.controller.interfaces.ITransactionController;
import com.ironhack.midterm.dao.users.User;
import com.ironhack.midterm.enums.AccountType;
import com.ironhack.midterm.repository.AccountRepository;
import com.ironhack.midterm.repository.UserRepository;
import com.ironhack.midterm.service.interfaces.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
public class TransactionController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ITransactionService transactionService;

    @GetMapping("/myaccount/balance") // Need to review SQL statement, as not currently working
    @ResponseStatus(HttpStatus.OK)
    public Long getBalanceByUser(@RequestParam String username, @RequestParam AccountType type) {
        return userRepository.getBalanceByUsername(username, type);
    }

    @GetMapping("/accounts/getbalance/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BigDecimal getBalanceById(@PathVariable(name = "id") long id) {
        return accountRepository.findBalanceById(id);
    }

}

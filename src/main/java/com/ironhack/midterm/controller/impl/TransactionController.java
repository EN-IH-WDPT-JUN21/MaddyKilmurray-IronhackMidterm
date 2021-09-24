package com.ironhack.midterm.controller.impl;

import com.ironhack.midterm.controller.interfaces.ITransactionController;
import com.ironhack.midterm.dao.Money;
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

    @GetMapping("/accounts/getbalance/checking/{account_id}")
    @ResponseStatus(HttpStatus.OK)
    public Money getCheckingBalance(@PathVariable(name = "account_id") long id, @RequestParam String username) {
        return transactionService.retrieveCheckingBalance(id, username);
    }

    @GetMapping("/accounts/getbalance/studentchecking/{account_id}")
    @ResponseStatus(HttpStatus.OK)
    public Money getStudentCheckingBalance(@PathVariable(name = "account_id") long id, @RequestParam String username) {
        return transactionService.retrieveStudentCheckingBalance(id, username);
    }

    @GetMapping("/accounts/getbalance/savings/{account_id}")
    @ResponseStatus(HttpStatus.OK)
    public Money getSavingsBalance(@PathVariable(name = "account_id") long id, @RequestParam String username) {
        return transactionService.retrieveSavingsBalance(id, username);
    }

    @GetMapping("/accounts/getbalance/creditcard/{account_id}")
    @ResponseStatus(HttpStatus.OK)
    public Money getCreditCardBalance(@PathVariable(name = "account_id") long id, @RequestParam String username) {
        return transactionService.retrieveCreditCardBalance(id, username);
    }

}

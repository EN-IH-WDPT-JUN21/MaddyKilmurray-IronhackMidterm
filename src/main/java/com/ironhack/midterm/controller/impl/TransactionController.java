package com.ironhack.midterm.controller.impl;

import com.ironhack.midterm.controller.dto.MoneyDTO;
import com.ironhack.midterm.dao.Money;
import com.ironhack.midterm.repository.accounts.AccountRepository;
import com.ironhack.midterm.repository.users.UserRepository;
import com.ironhack.midterm.service.interfaces.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public MoneyDTO getCheckingBalance(@PathVariable(name = "account_id") long id, @RequestParam String username) {
        return transactionService.retrieveCheckingBalance(id, username);
    }

    @GetMapping("/accounts/getbalance/studentchecking/{account_id}")
    @ResponseStatus(HttpStatus.OK)
    public MoneyDTO getStudentCheckingBalance(@PathVariable(name = "account_id") long id, @RequestParam String username) {
        return transactionService.retrieveStudentCheckingBalance(id, username);
    }

    @GetMapping("/accounts/getbalance/savings/{account_id}")
    @ResponseStatus(HttpStatus.OK)
    public MoneyDTO getSavingsBalance(@PathVariable(name = "account_id") long id, @RequestParam String username) {
        return transactionService.retrieveSavingsBalance(id, username);
    }

    @GetMapping("/accounts/getbalance/creditcard/{account_id}")
    @ResponseStatus(HttpStatus.OK)
    public MoneyDTO getCreditCardBalance(@PathVariable(name = "account_id") long id, @RequestParam String username) {
        return transactionService.retrieveCreditCardBalance(id, username);
    }

    @GetMapping("/accounts/getbalance/thirdparty/{account_id}")
    @ResponseStatus(HttpStatus.OK)
    public MoneyDTO getThirdPartyBalance(@PathVariable(name = "account_id") long id, @RequestParam String username) {
        return transactionService.retrieveThirdPartyBalance(id, username);
    }

    

}

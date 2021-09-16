package com.ironhack.midterm.controller.impl;

import com.ironhack.midterm.controller.interfaces.ICheckingAccountController;
import com.ironhack.midterm.repository.CheckingAccountRepository;
import com.ironhack.midterm.service.interfaces.ICheckingAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckingAccountController implements ICheckingAccountController {

    @Autowired
    private CheckingAccountRepository checkingAccountRepository;

    @Autowired
    private ICheckingAccountService checkingAccountService;

    @GetMapping("/hello-world")
    @ResponseStatus(HttpStatus.OK)
    public String helloWorld() {
        return "Hello world!! This API is working!";
    }
}

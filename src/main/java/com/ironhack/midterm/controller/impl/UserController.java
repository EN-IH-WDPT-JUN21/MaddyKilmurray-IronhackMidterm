package com.ironhack.midterm.controller.impl;

import com.ironhack.midterm.dao.users.User;
import com.ironhack.midterm.repository.AccountHolderRepository;
import com.ironhack.midterm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<User> getAllUsersByUsername(String name) {
        return userRepository.findByUsername(name);
    }

    @GetMapping("/myaccount/balance")
    @ResponseStatus(HttpStatus.OK)
    public Optional<User> getBalanceByUser(@RequestParam String username) {
        return userRepository.getBalanceByUsername(username);
    }
}

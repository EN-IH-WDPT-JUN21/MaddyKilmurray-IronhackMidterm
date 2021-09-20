package com.ironhack.midterm.controller.impl;

import com.ironhack.midterm.dao.users.User;
import com.ironhack.midterm.dao.users.usersubclasses.AccountHolder;
import com.ironhack.midterm.dao.users.usersubclasses.Admin;
import com.ironhack.midterm.dao.users.usersubclasses.ThirdParty;
import com.ironhack.midterm.repository.AccountHolderRepository;
import com.ironhack.midterm.repository.UserRepository;
import com.ironhack.midterm.service.impl.UserService;
import com.ironhack.midterm.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Autowired
    private IUserService userService;

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<User> getAllUsersByUsername(@PathVariable String name) {
        return userRepository.findByUsername(name);
    }

    @GetMapping("/users/byid/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<User> getAllUsersById(@PathVariable Long id) {
        return userRepository.findById(id);
    }

    @GetMapping("/myaccount/balance")
    @ResponseStatus(HttpStatus.OK)
    public Optional<User> getBalanceByUser(@RequestParam String username) {
        return userRepository.getBalanceByUsername(username);
    }

    @PostMapping("/users/new/admin")
    @ResponseStatus(HttpStatus.CREATED)
    public User createNewAdmin(@RequestBody @Valid Admin admin) {
        return userService.createNewAdmin(admin);
    }

    @PostMapping("/users/new/accountholder")
    @ResponseStatus(HttpStatus.CREATED)
    public User createNewAccountHolder(@RequestBody @Valid AccountHolder accountHolder) {
        return userService.createNewAccountHolder(accountHolder);
    }

    @PostMapping("/users/new/thirdparty")
    @ResponseStatus(HttpStatus.CREATED)
    public User createNewThirdParty(@RequestBody @Valid ThirdParty thirdParty) {
        return userService.createNewThirdParty(thirdParty);
    }
}
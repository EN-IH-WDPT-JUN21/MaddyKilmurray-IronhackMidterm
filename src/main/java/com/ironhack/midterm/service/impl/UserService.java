package com.ironhack.midterm.service.impl;

import com.ironhack.midterm.dao.users.User;
import com.ironhack.midterm.dao.users.usersubclasses.AccountHolder;
import com.ironhack.midterm.dao.users.usersubclasses.Admin;
import com.ironhack.midterm.dao.users.usersubclasses.ThirdParty;
import com.ironhack.midterm.repository.UserRepository;
import com.ironhack.midterm.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    public User createNewAdmin(Admin admin) {
        Admin newUser = new Admin(admin.getName(), admin.getUsername(), admin.getPassword(),
                admin.getRoles());
        return userRepository.save(newUser);
    }

    public User createNewAccountHolder(AccountHolder accountHolder) {
        AccountHolder newUser = new AccountHolder(accountHolder.getName(), accountHolder.getUsername(),
                accountHolder.getPassword(), accountHolder.getRoles(), accountHolder.getDateOfBirth(), accountHolder.getPrimaryAddress(),
                accountHolder.getMailingAddress(), accountHolder.getAccounts());
        return userRepository.save(newUser);
    }

    public User createNewThirdParty(ThirdParty thirdParty) {
        ThirdParty newUser = new ThirdParty(thirdParty.getName(), thirdParty.getUsername(), thirdParty.getPassword(),
                thirdParty.getRoles(), thirdParty.getHashedKey());
        return userRepository.save(newUser);
    }

    public User update(Long id,AccountHolder accountHolder) {
        Optional<User> foundUser = userRepository.findById(id);
        if (!foundUser.isPresent() || !(foundUser.get() instanceof AccountHolder)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Account holder doesn't exist.");
        }
        if (accountHolder.getName()!=null) {
            foundUser.get().setName(accountHolder.getName());
        }
        if (accountHolder.getUsername()!=null) {
            foundUser.get().setUsername(accountHolder.getUsername());
        }
        if (accountHolder.getPassword() != null) {
            foundUser.get().setPassword(accountHolder.getPassword());
        }
        if (accountHolder.getRoles() != null) {
            foundUser.get().setRoles(accountHolder.getRoles());
        }
        if (accountHolder.getAccounts()!=null) {
            ((AccountHolder) foundUser.get()).setAccounts(accountHolder.getAccounts());
        }
        if (accountHolder.getDateOfBirth() != null) {
            ((AccountHolder) foundUser.get()).setDateOfBirth(accountHolder.getDateOfBirth());
        }
        if (accountHolder.getMailingAddress() != null) {
            ((AccountHolder) foundUser.get()).setMailingAddress(accountHolder.getMailingAddress());
        }
        if (accountHolder.getPrimaryAddress() != null) {
            ((AccountHolder) foundUser.get()).setPrimaryAddress(accountHolder.getPrimaryAddress());
        }
        return userRepository.save(foundUser.get());
    }

    public void remove(Long id) {
        Optional<User> foundUser = Optional.of(userRepository.getById(id));
        if (foundUser.isPresent()) {
            userRepository.delete(foundUser.get());
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User doesn't exist");
        }
    }
}

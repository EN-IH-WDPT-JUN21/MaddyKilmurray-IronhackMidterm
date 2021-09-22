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

    public void updateUsernameAndPassword(Long id,Optional<String> username, Optional<String> password) {
        Optional<User> foundUser = userRepository.findById(id);
        if (!foundUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User doesn't exist");
        }
        if (username.isPresent()) {
            foundUser.get().setUsername(username.get());
        }
        if (password.isPresent()) {
            foundUser.get().setUsername(username.get());
        }
        userRepository.save(foundUser.get());
    }

    public User updateHolder(Long id,AccountHolder accountHolder) {
        Optional<User> foundUser = userRepository.findById(id);
        if (!foundUser.isPresent() || !(foundUser.get() instanceof AccountHolder)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Account holder doesn't exist.");
        }
        if (!accountHolder.getName().isBlank() || !accountHolder.getName().equals(" ")) {
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

    public User updateThirdParty(Long id,ThirdParty thirdParty) {
        Optional<User> foundUser = userRepository.findById(id);
        if (!foundUser.isPresent() || !(foundUser.get() instanceof ThirdParty)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Third Party doesn't exist.");
        }
        if (thirdParty.getName().isBlank() || !thirdParty.getName().equals(" ")) {
            foundUser.get().setName(thirdParty.getName());
        }
        if (thirdParty.getUsername()!=null) {
            foundUser.get().setUsername(thirdParty.getUsername());
        }
        if (thirdParty.getPassword() != null) {
            foundUser.get().setPassword(thirdParty.getPassword());
        }
        if (thirdParty.getRoles() != null) {
            foundUser.get().setRoles(thirdParty.getRoles());
        }
        if (!thirdParty.getHashedKey().isBlank() || !thirdParty.getHashedKey().equals(" ")) {
            ((ThirdParty) foundUser.get()).setHashedKey(thirdParty.getHashedKey());
        }
        return userRepository.save(foundUser.get());
    }

    public User updateAdmin(Long id,Admin admin) {
        Optional<User> foundUser = userRepository.findById(id);
        if (!foundUser.isPresent() || !(foundUser.get() instanceof Admin)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Admin doesn't exist.");
        }
        if (!admin.getName().isBlank() || !admin.getName().equals(" ")) {
            foundUser.get().setName(admin.getName());
        }
        if (admin.getUsername()!=null) {
            foundUser.get().setUsername(admin.getUsername());
        }
        if (admin.getPassword() != null) {
            foundUser.get().setPassword(admin.getPassword());
        }
        if (admin.getRoles() != null) {
            foundUser.get().setRoles(admin.getRoles());
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

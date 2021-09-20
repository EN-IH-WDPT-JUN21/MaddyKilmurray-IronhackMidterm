package com.ironhack.midterm.service.impl;

import com.ironhack.midterm.dao.users.User;
import com.ironhack.midterm.dao.users.usersubclasses.AccountHolder;
import com.ironhack.midterm.dao.users.usersubclasses.Admin;
import com.ironhack.midterm.dao.users.usersubclasses.ThirdParty;
import com.ironhack.midterm.repository.UserRepository;
import com.ironhack.midterm.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}

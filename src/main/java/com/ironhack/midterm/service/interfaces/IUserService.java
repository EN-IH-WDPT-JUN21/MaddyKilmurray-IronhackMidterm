package com.ironhack.midterm.service.interfaces;

import com.ironhack.midterm.dao.users.User;
import com.ironhack.midterm.dao.users.usersubclasses.AccountHolder;
import com.ironhack.midterm.dao.users.usersubclasses.Admin;
import com.ironhack.midterm.dao.users.usersubclasses.ThirdParty;

import java.util.Optional;

public interface IUserService {

    User createNewAdmin(Admin admin);
    User createNewAccountHolder(AccountHolder accountHolder);
    User createNewThirdParty(ThirdParty thirdParty);
    void updateUsernameAndPassword(Long id,Optional<String> username, Optional<String> password);
    User updateHolder(Long id,AccountHolder accountHolder);
    User updateThirdParty(Long id,ThirdParty thirdParty);
    User updateAdmin(Long id,Admin admin);
    void remove(Long id);
}

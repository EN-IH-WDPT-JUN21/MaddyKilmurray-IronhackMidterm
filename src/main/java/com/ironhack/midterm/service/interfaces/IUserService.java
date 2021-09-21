package com.ironhack.midterm.service.interfaces;

import com.ironhack.midterm.dao.users.User;
import com.ironhack.midterm.dao.users.usersubclasses.AccountHolder;
import com.ironhack.midterm.dao.users.usersubclasses.Admin;
import com.ironhack.midterm.dao.users.usersubclasses.ThirdParty;

public interface IUserService {

    User createNewAdmin(Admin admin);
    User createNewAccountHolder(AccountHolder accountHolder);
    User createNewThirdParty(ThirdParty thirdParty);
    User update(Long id,AccountHolder accountHolder);
    void remove(Long id);
}

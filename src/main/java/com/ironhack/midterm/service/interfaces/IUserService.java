package com.ironhack.midterm.service.interfaces;

import com.ironhack.midterm.dao.users.User;
import com.ironhack.midterm.dao.users.usersubclasses.AccountHolder;
import com.ironhack.midterm.dao.users.usersubclasses.Admin;
import com.ironhack.midterm.dao.users.usersubclasses.ThirdParty;
import com.ironhack.midterm.service.impl.AccountHolderService;

public interface IUserService {

    User createNewAdmin(Admin admin);
    User createNewAccountHolder(AccountHolder accountHolder);
    User createNewThirdParty(ThirdParty thirdParty);
}

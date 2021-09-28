package com.ironhack.midterm.service.interfaces;

import com.ironhack.midterm.controller.dto.users.AccountHolderDTO;
import com.ironhack.midterm.controller.dto.users.AdminDTO;
import com.ironhack.midterm.controller.dto.users.ThirdPartyDTO;
import com.ironhack.midterm.dao.users.User;
import com.ironhack.midterm.dao.users.usersubclasses.AccountHolder;
import com.ironhack.midterm.dao.users.usersubclasses.Admin;
import com.ironhack.midterm.dao.users.usersubclasses.ThirdParty;

import java.util.Optional;

public interface IUserService {

    AdminDTO createNewAdmin(AdminDTO admin);
    AccountHolderDTO createNewAccountHolder(AccountHolderDTO accountHolder);
    ThirdPartyDTO createNewThirdParty(ThirdPartyDTO thirdParty);
    void updateUsernameAndPassword(Long id,String username, String password);
    void updateUsername(Long id,String username);
    void updatePassword(Long id,String password);
    AccountHolderDTO updateHolder(Long id,AccountHolderDTO accountHolder);
    ThirdPartyDTO updateThirdParty(Long id,ThirdPartyDTO thirdParty);
    AdminDTO updateAdmin(Long id,AdminDTO admin);
    void remove(Long id);
}

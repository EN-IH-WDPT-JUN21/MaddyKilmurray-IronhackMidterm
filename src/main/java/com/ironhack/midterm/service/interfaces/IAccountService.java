package com.ironhack.midterm.service.interfaces;

import com.ironhack.midterm.dao.accounts.Account;

public interface IAccountService {

    Account createNewAccount(String accountType, Account account);
}

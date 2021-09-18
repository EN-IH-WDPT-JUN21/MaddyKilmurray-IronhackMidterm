package com.ironhack.midterm.service.impl;

import com.ironhack.midterm.dao.accounts.Account;
import com.ironhack.midterm.service.interfaces.IAccountService;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements IAccountService {

    public Account createNewAccount(String accountType, Account account) {
        if (account == null) {
            return null;
        }
        if (accountType.equalsIgnoreCase("CHECKING")) {
            if (account.getPrimaryOwner().isUnder24()) {
                return createStudentAccount();
            }
            return createCheckingAccount();
        }
        if (accountType.equalsIgnoreCase("SAVINGS")) {
            return createSavingsAccount();
        }
        if (accountType.equalsIgnoreCase("CREDITCARD")) {
            return createCreditCardAccount();
        }
        return null;
    }
}

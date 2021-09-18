package com.ironhack.midterm.service.impl;

import com.ironhack.midterm.dao.accounts.Account;
import com.ironhack.midterm.service.interfaces.IAccountService;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements IAccountService {

//    public Account createNewAccount(String accountType, Account account) {
//        if (account == null) {
//            return null;
//        }
//        if (accountType.equalsIgnoreCase("CHECKING")) {
//            if (account.getPrimaryOwner().isUnder24()) {
//                return createStudentAccount(account);
//            }
//            return createCheckingAccount(account);
//        }
//        if (accountType.equalsIgnoreCase("SAVINGS")) {
//            return createSavingsAccount(account);
//        }
//        if (accountType.equalsIgnoreCase("CREDITCARD")) {
//            return createCreditCardAccount(account);
//        }
//        return null;
//    }
}

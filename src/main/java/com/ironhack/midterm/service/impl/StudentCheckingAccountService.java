package com.ironhack.midterm.service.impl;

import com.ironhack.midterm.dao.accounts.Account;
import com.ironhack.midterm.dao.accounts.accountsubclasses.StudentCheckingAccount;
import com.ironhack.midterm.service.interfaces.IStudentCheckingAccountService;
import org.springframework.stereotype.Service;

@Service
public class StudentCheckingAccountService implements IStudentCheckingAccountService {

//    public Account createStudentAccount(Account account) {
//        String secretKey = generateSecretKey();
//        Account studentAccount = new StudentCheckingAccount(account.getBalance(), account.getPrimaryOwner(),
//                account.getSecondaryOwner(), account.getPenaltyFee(), account.getCreationDate(),account.getStatus(),secretKey);
//    }
}

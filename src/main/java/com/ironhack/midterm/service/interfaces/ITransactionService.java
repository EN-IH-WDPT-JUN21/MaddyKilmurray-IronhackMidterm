package com.ironhack.midterm.service.interfaces;

import com.ironhack.midterm.dao.Money;

import java.math.BigDecimal;

public interface ITransactionService {

    Money retrieveCheckingBalance(long accountid, String username);
    Money retrieveStudentCheckingBalance(long accountid, String username);
    Money retrieveSavingsBalance(long accountid, String username);
    Money retrieveCreditCardBalance(long accountid, String username);
    Money retrieveThirdPartyBalance(long accountid, String username);

}

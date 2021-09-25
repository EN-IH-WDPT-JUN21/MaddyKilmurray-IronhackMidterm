package com.ironhack.midterm.service.interfaces;

import com.ironhack.midterm.controller.dto.MoneyDTO;
import com.ironhack.midterm.dao.Money;
import com.ironhack.midterm.exceptions.BalanceOutOfBoundsException;

import java.math.BigDecimal;

public interface ITransactionService {

    MoneyDTO retrieveCheckingBalance(long accountid, String username);
    MoneyDTO retrieveStudentCheckingBalance(long accountid, String username);
    MoneyDTO retrieveSavingsBalance(long accountid, String username);
    MoneyDTO retrieveCreditCardBalance(long accountid, String username);
    MoneyDTO retrieveThirdPartyBalance(long accountid, String username);
    void transferFunds(long transferAccountId, long receivingAccountId, MoneyDTO transferRequest) throws BalanceOutOfBoundsException;
}

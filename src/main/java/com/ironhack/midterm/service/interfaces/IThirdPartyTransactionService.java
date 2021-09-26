package com.ironhack.midterm.service.interfaces;

import com.ironhack.midterm.controller.dto.MoneyDTO;
import com.ironhack.midterm.controller.dto.TransactionDTO;
import com.ironhack.midterm.exceptions.BalanceOutOfBoundsException;

public interface IThirdPartyTransactionService {

    MoneyDTO retrieveThirdPartyBalance(long accountid, String username);

    void transferFundsThirdParty(String username, String hashedKey, TransactionDTO transactionDTO) throws BalanceOutOfBoundsException;
}

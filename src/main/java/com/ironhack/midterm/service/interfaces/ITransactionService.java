package com.ironhack.midterm.service.interfaces;

import com.ironhack.midterm.controller.dto.MoneyDTO;
import com.ironhack.midterm.controller.dto.transactions.TransactionDTO;

public interface ITransactionService {

    MoneyDTO retrieveCheckingBalance(long accountid, String username);
    MoneyDTO retrieveStudentCheckingBalance(long accountid, String username);
    MoneyDTO retrieveSavingsBalance(long accountid, String username);
    MoneyDTO retrieveCreditCardBalance(long accountid, String username);
    void transferFunds(TransactionDTO transactionDTO);
    void transferFundsAccHolder(String username, TransactionDTO transactionDTO);
}

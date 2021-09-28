package com.ironhack.midterm.service.interfaces;

import com.ironhack.midterm.controller.dto.MoneyDTO;
import com.ironhack.midterm.controller.dto.transactions.ThirdPartyTransactionDTO;
import com.ironhack.midterm.controller.dto.accounts.ThirdPartyAccountDTO;
import com.ironhack.midterm.dao.Money;
import com.ironhack.midterm.dao.transactions.ThirdPartyTransaction;
import com.ironhack.midterm.dao.transactions.Transaction;
import com.ironhack.midterm.dao.accounts.Account;
import com.ironhack.midterm.dao.accounts.accountsubclasses.ThirdPartyAccount;

import java.math.BigDecimal;

public interface IThirdPartyTransactionService {

    MoneyDTO retrieveThirdPartyBalance(long accountid, String username);
    ThirdPartyAccountDTO convertToThirdPartyAccountDto(ThirdPartyAccount account);
    BigDecimal transactionTotalInLastDay(ThirdPartyAccount account);
    BigDecimal findHighestDailyTotal(ThirdPartyAccount account);
    Boolean fraudFound(ThirdPartyAccount account, Transaction transaction);
    Boolean applyPenaltyFee(ThirdPartyAccount transferAccount, Transaction transaction);
    void successfulThirdPartyTransaction(ThirdPartyAccount transferAccount, Account receivingAccount, ThirdPartyTransaction transaction);
    void failedThirdPartyTransaction(ThirdPartyAccount account, ThirdPartyTransaction transaction);
    void transferFundsThirdParty(String hashedKey, ThirdPartyTransactionDTO transactionDTO);
    ThirdPartyTransaction convertToThirdPartyTransaction(ThirdPartyTransactionDTO transactionDTO);
    MoneyDTO convertToMoneyDto(Money money);
    String findSecretKey(Account account);
}

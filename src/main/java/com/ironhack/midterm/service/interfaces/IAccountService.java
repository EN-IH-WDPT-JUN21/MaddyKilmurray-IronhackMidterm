package com.ironhack.midterm.service.interfaces;

import com.ironhack.midterm.dao.accounts.Account;
import com.ironhack.midterm.dao.accounts.accountsubclasses.CheckingAccount;
import com.ironhack.midterm.dao.accounts.accountsubclasses.CreditCardAccount;
import com.ironhack.midterm.dao.accounts.accountsubclasses.SavingsAccount;
import com.ironhack.midterm.dao.accounts.accountsubclasses.ThirdPartyAccount;

public interface IAccountService {
    Account createNewCheckingAccount(CheckingAccount account);
    Account createNewSavingsAccount(SavingsAccount account);
    Account createNewCreditCardAccount(CreditCardAccount account);
    Account createNewThirdPartyAccount(ThirdPartyAccount account);
}

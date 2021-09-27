package com.ironhack.midterm.service.interfaces;

import com.ironhack.midterm.controller.dto.accounts.*;
import com.ironhack.midterm.dao.accounts.accountsubclasses.CheckingAccount;
import com.ironhack.midterm.dao.accounts.accountsubclasses.CreditCardAccount;
import com.ironhack.midterm.dao.accounts.accountsubclasses.SavingsAccount;
import com.ironhack.midterm.dao.accounts.accountsubclasses.ThirdPartyAccount;
import com.ironhack.midterm.exceptions.BalanceOutOfBoundsException;

public interface IAccountService {
    AccountDTO createNewCheckingAccount(CheckingAccountDTO account);
    AccountDTO createNewSavingsAccount(SavingsAccountDTO account);
    AccountDTO createNewCreditCardAccount(CreditCardAccountDTO account);
    ThirdPartyAccountDTO createNewThirdPartyAccount(ThirdPartyAccountDTO account);
}

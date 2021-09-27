package com.ironhack.midterm.service.interfaces;

import com.ironhack.midterm.controller.dto.accounts.AccountDTO;
import com.ironhack.midterm.controller.dto.accounts.ThirdPartyAccountDTO;
import com.ironhack.midterm.dao.accounts.accountsubclasses.CheckingAccount;
import com.ironhack.midterm.dao.accounts.accountsubclasses.CreditCardAccount;
import com.ironhack.midterm.dao.accounts.accountsubclasses.SavingsAccount;
import com.ironhack.midterm.dao.accounts.accountsubclasses.ThirdPartyAccount;

public interface IAccountService {
    AccountDTO createNewCheckingAccount(CheckingAccount account);
    AccountDTO createNewSavingsAccount(SavingsAccount account);
    AccountDTO createNewCreditCardAccount(CreditCardAccount account);
    ThirdPartyAccountDTO createNewThirdPartyAccount(ThirdPartyAccount account);
}

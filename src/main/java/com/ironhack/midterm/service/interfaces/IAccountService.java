package com.ironhack.midterm.service.interfaces;

import com.ironhack.midterm.controller.dto.accounts.*;
import com.ironhack.midterm.enums.Status;

public interface IAccountService {
    AccountDTO createNewCheckingAccount(CheckingAccountDTO account);
    AccountDTO createNewSavingsAccount(SavingsAccountDTO account);
    AccountDTO createNewCreditCardAccount(CreditCardAccountDTO account);
    ThirdPartyAccountDTO createNewThirdPartyAccount(ThirdPartyAccountDTO account);
    Status updateStatus(long id);
}

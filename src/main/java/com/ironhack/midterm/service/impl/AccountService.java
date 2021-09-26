package com.ironhack.midterm.service.impl;

import com.ironhack.midterm.controller.dto.accounts.AccountDTO;
import com.ironhack.midterm.controller.dto.users.AdminDTO;
import com.ironhack.midterm.dao.accounts.Account;
import com.ironhack.midterm.dao.accounts.accountsubclasses.CheckingAccount;
import com.ironhack.midterm.dao.accounts.accountsubclasses.CreditCardAccount;
import com.ironhack.midterm.dao.accounts.accountsubclasses.SavingsAccount;
import com.ironhack.midterm.dao.accounts.accountsubclasses.StudentCheckingAccount;
import com.ironhack.midterm.dao.accounts.accountsubclasses.ThirdPartyAccount;
import com.ironhack.midterm.dao.users.usersubclasses.Admin;
import com.ironhack.midterm.repository.accounts.AccountRepository;
import com.ironhack.midterm.service.interfaces.IAccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements IAccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Account createNewCheckingAccount(CheckingAccount account) {
        if (account.getPrimaryOwner().isUnder24()) {
            StudentCheckingAccount createdAccount = new StudentCheckingAccount(account.getBalance(),
                    account.getPrimaryOwner(),account.getSecondaryOwner(),account.getSecretKey());
            return accountRepository.save(createdAccount);
        }
        else {
            CheckingAccount createdAccount = new CheckingAccount(account.getBalance(),
                    account.getPrimaryOwner(), account.getSecondaryOwner(), account.getSecretKey());
            return accountRepository.save(createdAccount);
        }
    }

    public Account createNewSavingsAccount(SavingsAccount account) {
        if (account.getInterestRate() != null) {
            try {
                SavingsAccount createdAccount = new SavingsAccount(account.getBalance(), account.getPrimaryOwner(),
                        account.getSecondaryOwner(), account.getSecretKey(), account.getInterestRate());
                return accountRepository.save(createdAccount);
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }
        else {
            try {
                SavingsAccount createdAccount = new SavingsAccount(account.getBalance(), account.getPrimaryOwner(),
                        account.getSecondaryOwner(), account.getSecretKey());
                return accountRepository.save(createdAccount);
            }
            catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }
        return null;
    }

    public Account createNewCreditCardAccount(CreditCardAccount account) {
        if (account.getInterestRate() != null && account.getCreditLimit() != null) {
            try {
                CreditCardAccount createdAccount = new CreditCardAccount(account.getBalance(),
                        account.getPrimaryOwner(),account.getSecondaryOwner(),account.getCreditLimit(),
                        account.getInterestRate());
                return accountRepository.save(createdAccount);
            }
            catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }
        else if (account.getInterestRate() == null && account.getCreditLimit() == null) {
            CreditCardAccount createdAccount = new CreditCardAccount(account.getBalance(), account.getPrimaryOwner(),
                    account.getSecondaryOwner());
            return accountRepository.save(createdAccount);
        }
        else {
            try {
                CreditCardAccount createdAccount = new CreditCardAccount(account.getBalance(),
                        account.getPrimaryOwner(),account.getSecondaryOwner(),account.getCreditLimit());
                return accountRepository.save(createdAccount);
            }
            catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }
        return null;
    }

    public Account createNewThirdPartyAccount(ThirdPartyAccount account) {
        ThirdPartyAccount createdAccount = new ThirdPartyAccount(account.getBalance(), account.getPrimaryOwner(),
                account.getSecondaryOwner(), account.getHashedKey(), account.getName());
        return accountRepository.save(createdAccount);
    }

    public AccountDTO convertToAccountDto(Account account) {
        AccountDTO accountDTO = modelMapper.map(account, AccountDTO.class);
        return accountDTO;
    }
}

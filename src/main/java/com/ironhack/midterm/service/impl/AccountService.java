package com.ironhack.midterm.service.impl;

import com.ironhack.midterm.controller.dto.accounts.*;
import com.ironhack.midterm.dao.accounts.Account;
import com.ironhack.midterm.dao.accounts.accountsubclasses.CheckingAccount;
import com.ironhack.midterm.dao.accounts.accountsubclasses.CreditCardAccount;
import com.ironhack.midterm.dao.accounts.accountsubclasses.SavingsAccount;
import com.ironhack.midterm.dao.accounts.accountsubclasses.StudentCheckingAccount;
import com.ironhack.midterm.dao.accounts.accountsubclasses.ThirdPartyAccount;
import com.ironhack.midterm.dao.users.usersubclasses.AccountHolder;
import com.ironhack.midterm.dao.users.usersubclasses.ThirdParty;
import com.ironhack.midterm.enums.Status;
import com.ironhack.midterm.repository.accounts.AccountRepository;
import com.ironhack.midterm.repository.accounts.ThirdPartyAccountRepository;
import com.ironhack.midterm.repository.users.AccountHolderRepository;
import com.ironhack.midterm.repository.users.ThirdPartyRepository;
import com.ironhack.midterm.repository.users.UserRepository;
import com.ironhack.midterm.service.interfaces.IAccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AccountService implements IAccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ThirdPartyAccountRepository thirdPartyAccountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Autowired
    private ModelMapper modelMapper;

    public AccountDTO createNewCheckingAccount(CheckingAccountDTO account) {
        CheckingAccount newAccount = convertToCheckingAccount(account);
        if (newAccount.getPrimaryOwner().isUnder24()) {
            StudentCheckingAccount createdAccount = new StudentCheckingAccount(newAccount.getBalance(),
                    newAccount.getPrimaryOwner(),newAccount.getSecondaryOwner(),newAccount.getSecretKey());
            return convertToAccountDto(accountRepository.save(createdAccount));
        }
        else {
            CheckingAccount createdAccount = new CheckingAccount(newAccount.getBalance(),
                    newAccount.getPrimaryOwner(), newAccount.getSecondaryOwner(), newAccount.getSecretKey());
            return convertToAccountDto(accountRepository.save(createdAccount));
        }
    }

    public AccountDTO createNewSavingsAccount(SavingsAccountDTO account) {
        SavingsAccount newAccount = convertToSavingsAccount(account);
        if (newAccount.getInterestRate() != null) {
            try {
                SavingsAccount createdAccount = new SavingsAccount(newAccount.getBalance(), newAccount.getPrimaryOwner(),
                        newAccount.getSecondaryOwner(), newAccount.getSecretKey(), newAccount.getInterestRate());
                return convertToAccountDto(accountRepository.save(createdAccount));
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }
        else {
            try {
                SavingsAccount createdAccount = new SavingsAccount(newAccount.getBalance(), newAccount.getPrimaryOwner(),
                        newAccount.getSecondaryOwner(), newAccount.getSecretKey());
                return convertToAccountDto(accountRepository.save(createdAccount));
            }
            catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }
        return null;
    }

    public AccountDTO createNewCreditCardAccount(CreditCardAccountDTO account) {
        CreditCardAccount newAccount = convertToCreditCardAccount(account);
        if (newAccount.getInterestRate() != null && newAccount.getCreditLimit() != null) {
            try {
                CreditCardAccount createdAccount = new CreditCardAccount(newAccount.getBalance(),
                        newAccount.getPrimaryOwner(),newAccount.getSecondaryOwner(),newAccount.getCreditLimit(),
                        newAccount.getInterestRate());
                return convertToAccountDto(accountRepository.save(createdAccount));
            }
            catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }
        else if (newAccount.getInterestRate() == null && newAccount.getCreditLimit() == null) {
            CreditCardAccount createdAccount = new CreditCardAccount(newAccount.getBalance(), newAccount.getPrimaryOwner(),
                    newAccount.getSecondaryOwner());
            return convertToAccountDto(accountRepository.save(createdAccount));
        }
        else {
            try {
                CreditCardAccount createdAccount = new CreditCardAccount(newAccount.getBalance(),
                        newAccount.getPrimaryOwner(),newAccount.getSecondaryOwner(),newAccount.getCreditLimit());
                return convertToAccountDto(accountRepository.save(createdAccount));
            }
            catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }
        return null;
    }

    public ThirdPartyAccountDTO createNewThirdPartyAccount(ThirdPartyAccountDTO account) {
        ThirdPartyAccount newAccount = convertToThirdPartyAccount(account);
        ThirdPartyAccount createdAccount = new ThirdPartyAccount(newAccount.getBalance(), newAccount.getPrimaryOwner(),
                newAccount.getSecondaryOwner(), newAccount.getHashedKey(), newAccount.getName());
        return convertToThirdPartyAccountDto(thirdPartyAccountRepository.save(createdAccount));
    }

    public AccountDTO convertToAccountDto(Account account) {
        AccountDTO accountDTO = modelMapper.map(account, AccountDTO.class);
        return accountDTO;
    }

    public ThirdPartyAccountDTO convertToThirdPartyAccountDto(ThirdPartyAccount account) {
        ThirdPartyAccountDTO accountDTO = modelMapper.map(account, ThirdPartyAccountDTO.class);
        return accountDTO;
    }

    public ThirdPartyAccount convertToThirdPartyAccount(ThirdPartyAccountDTO accountDTO) {
        Optional<ThirdParty> primaryOwner = thirdPartyRepository.findById(accountDTO.getPrimaryOwnerId());
        ThirdPartyAccount account = null;
        if (accountDTO.getSecondaryOwnerId() != null) {
            Optional<ThirdParty> secondaryOwner = thirdPartyRepository.findById(accountDTO.getSecondaryOwnerId());
            account = new ThirdPartyAccount(accountDTO.getBalance(), primaryOwner.get(), secondaryOwner.get(),
                    accountDTO.getHashedKey(), accountDTO.getName());
        }
        else {
            account = new ThirdPartyAccount(accountDTO.getBalance(), primaryOwner.get(),
                    accountDTO.getHashedKey(), accountDTO.getName());
        }
        return account;
    }

    public CheckingAccount convertToCheckingAccount(CheckingAccountDTO accountDTO) {
        Optional<AccountHolder> primaryOwner = accountHolderRepository.findById(accountDTO.getPrimaryOwnerId());
        CheckingAccount account = null;
        if (accountDTO.getSecondaryOwnerId() != null) {
            Optional<AccountHolder> secondaryOwner = accountHolderRepository.findById(accountDTO.getSecondaryOwnerId());
            account = new CheckingAccount(accountDTO.getBalance(), primaryOwner.get(), secondaryOwner.get(),
                    accountDTO.getSecretKey());
        }
        else {
            account = new CheckingAccount(accountDTO.getBalance(), primaryOwner.get(), accountDTO.getSecretKey());
        }
        return account;
    }

    public SavingsAccount convertToSavingsAccount(SavingsAccountDTO accountDTO) {
        Optional<AccountHolder> primaryOwner = accountHolderRepository.findById(accountDTO.getPrimaryOwnerId());
        SavingsAccount account = null;
        if (accountDTO.getSecondaryOwnerId() != null) {
            Optional<AccountHolder> secondaryOwner = accountHolderRepository.findById(accountDTO.getSecondaryOwnerId());
            if (accountDTO.getInterestRate().equals(null)) {
                account = new SavingsAccount(accountDTO.getBalance(), primaryOwner.get(), secondaryOwner.get(),
                        accountDTO.getSecretKey());
            } else {
                account = new SavingsAccount(accountDTO.getBalance(), primaryOwner.get(), secondaryOwner.get(),
                        accountDTO.getSecretKey(), accountDTO.getInterestRate());
            }
        }
        else {
            if (accountDTO.getInterestRate() == null) {
                account = new SavingsAccount(accountDTO.getBalance(), primaryOwner.get(),
                        accountDTO.getSecretKey());
            } else {
                account = new SavingsAccount(accountDTO.getBalance(), primaryOwner.get(),
                        accountDTO.getSecretKey(), accountDTO.getInterestRate());
            }
        }
        return account;
    }

    public CreditCardAccount convertToCreditCardAccount(CreditCardAccountDTO accountDTO) {
        Optional<AccountHolder> primaryOwner = accountHolderRepository.findById(accountDTO.getPrimaryOwnerId());

        CreditCardAccount account = null;
        if (accountDTO.getSecondaryOwnerId() != null) {
            Optional<AccountHolder> secondaryOwner = accountHolderRepository.findById(accountDTO.getSecondaryOwnerId());
            if (accountDTO.getCreditLimit() != null && accountDTO.getInterestRate() != null) {
                account = new CreditCardAccount(accountDTO.getBalance(), primaryOwner.get(), secondaryOwner.get(),
                        accountDTO.getCreditLimit(), accountDTO.getInterestRate());
            } else if (accountDTO.getInterestRate() == null) {
                account = new CreditCardAccount(accountDTO.getBalance(), primaryOwner.get(), secondaryOwner.get(),
                        accountDTO.getCreditLimit());
            } else {
                account = new CreditCardAccount(accountDTO.getBalance(), primaryOwner.get(), secondaryOwner.get());
            }
        }
        else {
            if (accountDTO.getCreditLimit() != null && accountDTO.getInterestRate() != null) {
                account = new CreditCardAccount(accountDTO.getBalance(), primaryOwner.get(),
                        accountDTO.getCreditLimit(), accountDTO.getInterestRate());
            } else if (accountDTO.getInterestRate() == null) {
                account = new CreditCardAccount(accountDTO.getBalance(), primaryOwner.get(),
                        accountDTO.getCreditLimit());
            } else {
                account = new CreditCardAccount(accountDTO.getBalance(), primaryOwner.get());
            }
        }
        return account;
    }

    public Status updateStatus(long id) {
        Optional<Account> foundAccount = accountRepository.findById(id);
        if (foundAccount.isPresent()) {
            if (foundAccount.get().getStatus().equals(Status.FROZEN)) {
                foundAccount.get().setStatus(Status.ACTIVE);
            }
            else {
                foundAccount.get().setStatus(Status.FROZEN);
            }
            accountRepository.save(foundAccount.get());
            return foundAccount.get().getStatus();
        }
        Optional<ThirdPartyAccount> foundThirdParty = thirdPartyAccountRepository.findById(id);
        if (foundThirdParty.isPresent()) {
            if (foundThirdParty.get().getStatus().equals(Status.FROZEN)) {
                foundThirdParty.get().setStatus(Status.ACTIVE);
            }
            else {
                foundThirdParty.get().setStatus(Status.FROZEN);
            }
            thirdPartyAccountRepository.save(foundThirdParty.get());
            return foundThirdParty.get().getStatus();
        }
        if (!foundAccount.isPresent() && !foundThirdParty.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Account not found. Please try again.");
        }
        return null;
    }
}

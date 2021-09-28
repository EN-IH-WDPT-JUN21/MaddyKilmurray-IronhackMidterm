package com.ironhack.midterm.controller.impl;

import com.ironhack.midterm.controller.dto.MoneyDTO;
import com.ironhack.midterm.controller.dto.transactions.ThirdPartyTransactionDTO;
import com.ironhack.midterm.controller.dto.transactions.TransactionDTO;
import com.ironhack.midterm.controller.dto.accounts.AccountDTO;
import com.ironhack.midterm.controller.dto.accounts.ThirdPartyAccountDTO;
import com.ironhack.midterm.repository.accounts.AccountRepository;
import com.ironhack.midterm.repository.accounts.ThirdPartyAccountRepository;
import com.ironhack.midterm.repository.users.UserRepository;
import com.ironhack.midterm.service.impl.AccountService;
import com.ironhack.midterm.service.interfaces.IThirdPartyTransactionService;
import com.ironhack.midterm.service.interfaces.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class TransactionController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ThirdPartyAccountRepository thirdPartyAccountRepository;

    @Autowired
    private IThirdPartyTransactionService thirdPartyTransactionService;

    @Autowired
    private ITransactionService transactionService;

    @GetMapping("/accounts/getbalance/checking/{account_id}")
    @ResponseStatus(HttpStatus.OK)
    public MoneyDTO getCheckingBalance(@PathVariable(name = "account_id") long id, @RequestParam String username) {
        return transactionService.retrieveCheckingBalance(id, username);
    }

    @GetMapping("/accounts/getbalance/studentchecking/{account_id}")
    @ResponseStatus(HttpStatus.OK)
    public MoneyDTO getStudentCheckingBalance(@PathVariable(name = "account_id") long id, @RequestParam String username) {
        return transactionService.retrieveStudentCheckingBalance(id, username);
    }

    @GetMapping("/accounts/getbalance/savings/{account_id}")
    @ResponseStatus(HttpStatus.OK)
    public MoneyDTO getSavingsBalance(@PathVariable(name = "account_id") long id, @RequestParam String username) {
        return transactionService.retrieveSavingsBalance(id, username);
    }

    @GetMapping("/accounts/getbalance/creditcard/{account_id}")
    @ResponseStatus(HttpStatus.OK)
    public MoneyDTO getCreditCardBalance(@PathVariable(name = "account_id") long id, @RequestParam String username) {
        return transactionService.retrieveCreditCardBalance(id, username);
    }

    @GetMapping("/accounts/getbalance/thirdparty/{account_id}")
    @ResponseStatus(HttpStatus.OK)
    public MoneyDTO getThirdPartyBalance(@PathVariable(name = "account_id") long id, @RequestParam String username) {
        return thirdPartyTransactionService.retrieveThirdPartyBalance(id, username);
    }

    @PatchMapping("/accounts/admin/transferfunds/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public AccountDTO adminTransferFunds(@RequestBody @Valid TransactionDTO transaction) {
        transactionService.transferFunds(transaction);
        return accountService.convertToAccountDto(accountRepository.findById(transaction.getTransferAccountId()).get());
    }

    @PatchMapping("/accounts/accountholder/transferfunds/{username}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public AccountDTO accHolderTransferFunds(@PathVariable(name = "username") String username,
                                      @RequestBody @Valid TransactionDTO transaction) {
        transactionService.transferFundsAccHolder(username,transaction);
        return accountService.convertToAccountDto(accountRepository.findById(transaction.getTransferAccountId()).get());
    }

    @PatchMapping("/accounts/thirdparty/transferfunds/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ThirdPartyAccountDTO thirdPartyTransferFunds(@RequestParam String hashedKey,
                                                        @RequestBody @Valid ThirdPartyTransactionDTO transaction) {
        thirdPartyTransactionService.transferFundsThirdParty(hashedKey,transaction);
        return thirdPartyTransactionService.convertToThirdPartyAccountDto(thirdPartyAccountRepository.findById(transaction.getTransferAccountId()).get());
    }
}

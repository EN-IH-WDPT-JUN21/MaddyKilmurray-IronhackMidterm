package com.ironhack.midterm.repository;

import com.ironhack.midterm.dao.Money;
import com.ironhack.midterm.dao.accounts.Account;
import com.ironhack.midterm.repository.accounts.AccountRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    Account testAccount = null;

    @BeforeEach
    public void setUp() {
        testAccount = new Account(new Money(new BigDecimal(55.65), Currency.getInstance("GBP")));
        accountRepository.save(testAccount);
    }

    @AfterEach
    public void tearDown() {
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("Test: findById(). Positive Result")
    public void AccountRepository_FindByIdTest_PositiveResult() {
        Optional<Account> foundAccount = accountRepository.findById(testAccount.getId());
        assertTrue(foundAccount.isPresent());
    }

    @Test
    @DisplayName("Test: findById(). Negative Result")
    public void AccountRepository_FindByIdTest_NegativeResult() {
        Optional<Account> foundAccount = accountRepository.findById(99999);
        assertFalse(foundAccount.isPresent());
    }

    @Test
    @DisplayName("Test: findBalanceById(). Positive Result")
    public void AccountRepository_FindBalanceByIdTest_PositiveResult() {
        BigDecimal findBalance = accountRepository.findBalanceById(testAccount.getId());
        assertEquals(findBalance.setScale(2, RoundingMode.UP), new BigDecimal(55.65).setScale(2,RoundingMode.UP));
    }

    @Test
    @DisplayName("Test: findBalanceById(). Negative Result")
    public void AccountRepository_FindBalanceByIdTest_NegativeResult() {
        BigDecimal findBalance = accountRepository.findBalanceById(testAccount.getId());
        assertNotEquals(findBalance.setScale(2, RoundingMode.UP), new BigDecimal(55.66).setScale(2,RoundingMode.UP));
    }
}

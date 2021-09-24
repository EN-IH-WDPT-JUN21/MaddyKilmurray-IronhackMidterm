//package com.ironhack.midterm.repository;
//
//import com.ironhack.midterm.dao.Money;
//import com.ironhack.midterm.dao.accounts.Account;
//import org.aspectj.lang.annotation.Before;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.util.Currency;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest
//public class AccountRepositoryTest {
//
//    @Autowired
//    private AccountRepository accountRepository;
//
//    Account testAccount = null;
//
//    @BeforeEach
//    public void setUp() {
//        testAccount = new Account(new Money(new BigDecimal(55.65), Currency.getInstance("GBP")));
//        accountRepository.save(testAccount);
//    }
//
//    @Test
//    @DisplayName("Test: findAll(). Positive Result")
//    public void AccountRepository_FindAllTest_PositiveResult() {
//        long findAllCount = accountRepository.findAll().size();
//        assertEquals(findAllCount,1);
//    }
//
//    @Test
//    @DisplayName("Test: findBalanceById(). Positive Result")
//    public void AccountRepository_FindBalanceByIdTest_PositiveResult() {
//        BigDecimal findBalance = accountRepository.findBalanceById(testAccount.getId());
//        assertEquals(findBalance.setScale(2, RoundingMode.UP), new BigDecimal(55.65).setScale(2,RoundingMode.UP));
//    }
//}

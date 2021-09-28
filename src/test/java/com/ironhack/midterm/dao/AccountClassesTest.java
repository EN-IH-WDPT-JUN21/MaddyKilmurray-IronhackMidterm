package com.ironhack.midterm.dao;

import com.ironhack.midterm.dao.accounts.accountsubclasses.CheckingAccount;
import com.ironhack.midterm.dao.accounts.accountsubclasses.CreditCardAccount;
import com.ironhack.midterm.dao.users.usersubclasses.AccountHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.constraints.AssertTrue;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class AccountClassesTest {

    private AccountHolder testAccountHolder = null;

    private CheckingAccount testAccount = null;

    private CreditCardAccount testCreditCard = null;

    @BeforeEach
    public void setUp() {
        testAccountHolder = new AccountHolder("Mr Test","testUsername","testPassword",LocalDate.of(2000, Month.JULY,1), null, null, null);
    }

    @Test
    @DisplayName("Test: Checking Account, Apply Monthly Maintenance. Applies maintenance as expected.")
    public void CheckingAccount_ApplyMonthlyMaintenanceTest_Positive() {
        testAccount = new CheckingAccount(new Money(BigDecimal.valueOf(500.00), Currency.getInstance("GBP")),testAccountHolder,"secret");
        testAccount.setMonthlyMaintenanceFeeLastApplied(LocalDate.now().minusMonths(1));

        testAccount.applyMonthlyMaintenance();

        assertEquals(testAccount.getBalance().getAmount(),(BigDecimal.valueOf(500.00).setScale(2, RoundingMode.UP).add(BigDecimal.valueOf(12))));
    }

    @Test
    @DisplayName("Test: CreditCardAccount, set Interest Rate. Rate within limits, set as expected.")
    public void CreditCardAccount_SetInterestRate_Positive() {
        CreditCardAccount creditCardAccount = new CreditCardAccount(new Money(BigDecimal.valueOf(123.45).setScale(2,RoundingMode.UP),Currency.getInstance("GBP")),testAccountHolder,new Money(BigDecimal.valueOf(500.00),Currency.getInstance("GBP")));

        creditCardAccount.setInterestRate(new Money(BigDecimal.valueOf(0.12),Currency.getInstance("GBP")));

        assertEquals(creditCardAccount.getInterestRate().getAmount(),(BigDecimal.valueOf(0.12)));
    }

    @Test
    @DisplayName("Test: CreditCardAccount, set Interest Rate. Rate outside of limits, default set.")
    public void CreditCardAccount_SetInterestRate_Negative() {
        CreditCardAccount creditCardAccount = new CreditCardAccount(new Money(BigDecimal.valueOf(123.45).setScale(2,RoundingMode.UP),Currency.getInstance("GBP")),testAccountHolder,new Money(BigDecimal.valueOf(500.00),Currency.getInstance("GBP")));

        creditCardAccount.setInterestRate(new Money(BigDecimal.valueOf(0.5),Currency.getInstance("GBP")));

        assertEquals(creditCardAccount.getInterestRate().getAmount(),(BigDecimal.valueOf(0.2).setScale(2,RoundingMode.UP)));
    }

    @Test
    @DisplayName("Test: CreditCardAccount, set Credit Limite. Rate within limits, set as expected.")
    public void CreditCardAccount_SetCreditLimit_Positive() {
        CreditCardAccount creditCardAccount = new CreditCardAccount(new Money(BigDecimal.valueOf(123.45).setScale(2,RoundingMode.UP),Currency.getInstance("GBP")),testAccountHolder);

        creditCardAccount.setCreditLimit(new Money(BigDecimal.valueOf(500.00),Currency.getInstance("GBP")));

        assertEquals(creditCardAccount.getCreditLimit().getAmount(),(BigDecimal.valueOf(500.00).setScale(2,RoundingMode.UP)));
    }

    @Test
    @DisplayName("Test: CreditCardAccount, set Credit Limit. Rate outside of limits, too low.")
    public void CreditCardAccount_SetCreditLimit_Negative_TooLow() {
        CreditCardAccount creditCardAccount = new CreditCardAccount(new Money(BigDecimal.valueOf(123.45).setScale(2,RoundingMode.UP),Currency.getInstance("GBP")),testAccountHolder);

        creditCardAccount.setCreditLimit(new Money(BigDecimal.valueOf(5),Currency.getInstance("GBP")));

        assertEquals(creditCardAccount.getCreditLimit().getAmount(),(BigDecimal.valueOf(100).setScale(2,RoundingMode.UP)));
    }
}

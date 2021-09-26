package com.ironhack.midterm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midterm.dao.Address;
import com.ironhack.midterm.dao.Money;
import com.ironhack.midterm.dao.accounts.accountsubclasses.*;
import com.ironhack.midterm.dao.users.Role;
import com.ironhack.midterm.dao.users.usersubclasses.AccountHolder;
import com.ironhack.midterm.dao.users.usersubclasses.Admin;
import com.ironhack.midterm.dao.users.usersubclasses.ThirdParty;
import com.ironhack.midterm.exceptions.BalanceOutOfBoundsException;
import com.ironhack.midterm.repository.accounts.*;
import com.ironhack.midterm.repository.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TransactionServiceTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CheckingAccountRepository checkingAccountRepository;

    @Autowired
    private StudentCheckingAccountRepository studentCheckingAccountRepository;

    @Autowired
    private CreditCardAccountRepository creditCardAccountRepository;

    @Autowired
    private SavingsAccountRepository savingsAccountRepository;

    @Autowired
    private ThirdPartyAccountRepository thirdPartyAccountRepository;

    @Autowired
    private AccountRepository accountRepository;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Admin testAdmin = null;
    private AccountHolder testAccountHolder1 = null;
    private AccountHolder testAccountHolder2 = null;
    private ThirdParty testThirdParty = null;

    private CheckingAccount testCheckingAccount = null;
    private StudentCheckingAccount testStudentAccount = null;
    private SavingsAccount testSavingsAccount = null;
    private CreditCardAccount testCreditCard = null;
    private ThirdPartyAccount testThirdPartyAccount = null;

    private Address address1;
    private Address address2;

    @BeforeEach
    public void setUp() throws BalanceOutOfBoundsException {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        address1 = new Address(55,"Long Street","Manchester","M1 1AD","United Kingdom");
        address2 = new Address(2,"Short Avenue","Liverpool","L1 8JQ","United Kingdom");

        testAdmin = new Admin("Linda Ronstadt","Ronstadt","lind@",new HashSet<Role>());
        testAccountHolder1 = new AccountHolder("Ronda Grimes", "Ronda","gr1mes",
                new HashSet<Role>(), LocalDate.of(2005, Month.JANUARY, 8),address1,address2,
                new ArrayList<>());
        testAccountHolder2 = new AccountHolder("Melissa McCarthy", "McCarthy","melmel",
                new HashSet<Role>(),LocalDate.of(1970, Month.AUGUST, 26),address2,address1,
                new ArrayList<>());
        testThirdParty = new ThirdParty("Bust-A-Mortgage","mortgage","bust@",new HashSet<Role>(),
                "banana");
        userRepository.save(testAdmin);
        userRepository.save(testAccountHolder1);
        userRepository.save(testAccountHolder2);
        userRepository.save(testThirdParty);

        testCheckingAccount = new CheckingAccount(new Money(BigDecimal.valueOf(55.65), Currency.getInstance("GBP")),testAccountHolder1,"secretKey");
        testStudentAccount = new StudentCheckingAccount(new Money(BigDecimal.valueOf(55.65), Currency.getInstance("GBP")),testAccountHolder1,"studentSecretKey");
        testSavingsAccount = new SavingsAccount(new Money(BigDecimal.valueOf(55.65), Currency.getInstance("GBP")),testAccountHolder1,"secretKey");
        testCreditCard = new CreditCardAccount(new Money(BigDecimal.valueOf(55.65), Currency.getInstance("GBP")),testAccountHolder1);
        testThirdPartyAccount = new ThirdPartyAccount(new Money(BigDecimal.valueOf(55.65), Currency.getInstance("GBP")),testThirdParty,"hashedKey","Mortgages are us.");
        accountRepository.save(testCheckingAccount);
        accountRepository.save(testStudentAccount);
        accountRepository.save(testSavingsAccount);
        accountRepository.save(testCreditCard);
        thirdPartyAccountRepository.save(testThirdPartyAccount);
    }

    @Test
    @DisplayName("Test: retrieveCheckingBalance(). Retrieves checking balance as expected.")
    public void TransactionService_RetrieveCheckingBalance_PositiveCase() {
        Optional<CheckingAccount> foundAccount = checkingAccountRepository.findById(testCheckingAccount.getId());
        Money balance = foundAccount.get().getBalance();
        assertEquals(balance,new Money(BigDecimal.valueOf(55.65)));
    }
}

package com.ironhack.midterm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midterm.controller.dto.accounts.CheckingAccountDTO;
import com.ironhack.midterm.controller.dto.accounts.CreditCardAccountDTO;
import com.ironhack.midterm.controller.dto.accounts.SavingsAccountDTO;
import com.ironhack.midterm.controller.dto.accounts.ThirdPartyAccountDTO;
import com.ironhack.midterm.dao.Address;
import com.ironhack.midterm.dao.Money;
import com.ironhack.midterm.dao.accounts.Account;
import com.ironhack.midterm.dao.accounts.accountsubclasses.*;
import com.ironhack.midterm.dao.users.Role;
import com.ironhack.midterm.dao.users.usersubclasses.AccountHolder;
import com.ironhack.midterm.dao.users.usersubclasses.ThirdParty;
import com.ironhack.midterm.repository.accounts.AccountRepository;
import com.ironhack.midterm.repository.accounts.ThirdPartyAccountRepository;
import com.ironhack.midterm.repository.users.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class AccountControllerTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ThirdPartyAccountRepository thirdPartyAccountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private AccountHolder accountHolder;
    private AccountHolder accountHolder2;
    private AccountHolder accountHolder3;
    private ThirdParty testThirdParty;
    private ThirdParty testThirdParty2;
    private CheckingAccount testCheckingAccount;
    private StudentCheckingAccount testStudentCheckingAccount;
    private SavingsAccount testSavingsAccount;
    private CreditCardAccount testCreditCardAccount;
    private ThirdPartyAccount testThirdPartyAccount;

    private Address address1;
    private Address address2;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        address1 = new Address(55,"Long Street","Manchester","M1 1AD","United Kingdom");
        address2 = new Address(2,"Short Avenue","Liverpool","L1 8JQ","United Kingdom");

        accountHolder = new AccountHolder("Ronda Grimes", "Ronda","gr1mes",
                LocalDate.of(2005, Month.JANUARY, 8),address1,address2,
                new ArrayList<Account>());
        accountHolder2 = new AccountHolder("Barry White", "WhiteB","b@rry",
                LocalDate.of(1960, Month.AUGUST, 30),address1,address2,
                new ArrayList<Account>());
        accountHolder3 = new AccountHolder("Will Smith", "WSmith","sm1th",
                LocalDate.of(1980, Month.MARCH, 16),address1,address2,
                new ArrayList<Account>());
        testThirdParty = new ThirdParty("Will Smith", "WSmith","sm1th",
                "###hashedkey###");
        testThirdParty2 = new ThirdParty("Willow Smith", "WSmith2","sm1th2",
                "###hashedkey###");
        userRepository.save(accountHolder);
        userRepository.save(accountHolder2);
        userRepository.save(accountHolder3);
        userRepository.save(testThirdParty);
        userRepository.save(testThirdParty2);

        testCheckingAccount = new CheckingAccount(new Money(new BigDecimal(55.65), Currency.getInstance("GBP")),accountHolder,"BANANA");
        testStudentCheckingAccount = new StudentCheckingAccount(new Money(new BigDecimal(66.23),Currency.getInstance("GBP")),accountHolder,"BERRY");
        testSavingsAccount = new SavingsAccount(new Money(new BigDecimal(101.00),Currency.getInstance("GBP")),accountHolder2,"CHERRY");
        testCreditCardAccount = new CreditCardAccount(new Money(new BigDecimal(489.54),Currency.getInstance("GBP")),accountHolder3);
        testThirdPartyAccount = new ThirdPartyAccount(new Money(new BigDecimal(2154.16),Currency.getInstance("GBP")),testThirdParty,"###hashed###",
                "Test Mortgages");
        accountRepository.save(testCheckingAccount);
        accountRepository.save(testStudentCheckingAccount);
        accountRepository.save(testSavingsAccount);
        accountRepository.save(testCreditCardAccount);
        thirdPartyAccountRepository.save(testThirdPartyAccount);
    }

    @AfterEach
    public void tearDown() {
        accountRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Test: GET all accounts. Returns all users as expected.")
    void AccountController_GetAllAccounts_AsExpected() throws Exception{
        MvcResult mvcResult = mockMvc.perform( MockMvcRequestBuilders.get("/accounts"))
                .andExpect(status().isOk()).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("BANANA"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("BERRY"));
    }

    @Test
    @DisplayName("Test: GET account by id. Returns account as expected.")
    void AccountController_GetAccountById_AsExpected() throws Exception{
        MvcResult mvcResult = mockMvc.perform( MockMvcRequestBuilders.get("/accounts/byid/" + testCheckingAccount.getId()))
                .andExpect(status().isOk()).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains(testCheckingAccount.getSecretKey()));
    }

    @Test
    @DisplayName("Test: GET accounts by id. No user returns, as doesn't exist.")
    void AccountController_GetAccountById_NegativeResult() throws Exception{
        MvcResult mvcResult = mockMvc.perform( MockMvcRequestBuilders.get("/accounts/byid/" + 999))
                .andExpect(status().isOk()).andReturn();
        assertEquals(mvcResult.getResponse().getContentAsString(),(""));
    }

    @Test
    @DisplayName("Test: POST new Checking Account. Posts new Checking Account, one account holder")
    void AccountController_PostCheckingAccount_Created_OneAccountHolder() throws Exception {
        CheckingAccountDTO testCheckingAccount2 = new CheckingAccountDTO(new Money(new BigDecimal(102.65), Currency.getInstance("GBP")),accountHolder.getId(),"BABYMETAL");

        String body = objectMapper.writeValueAsString(testCheckingAccount2);

        MvcResult result = mockMvc.perform(
                post("/accounts/new/checking")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("102.65"));
    }

    @Test
    @DisplayName("Test: POST new Checking Account. Posts new Checking Account, two account holders")
    void AccountController_PostCheckingAccount_Created_TwoAccountHolders() throws Exception {
        CheckingAccountDTO testCheckingAccount2 = new CheckingAccountDTO(new Money(new BigDecimal(102.65), Currency.getInstance("GBP")),accountHolder.getId(),accountHolder2.getId(),"BABYMETAL");

        String body = objectMapper.writeValueAsString(testCheckingAccount2);

        MvcResult result = mockMvc.perform(
                post("/accounts/new/checking")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("102.65"));
    }

    @Test
    @DisplayName("Test: POST new Savings Account. Posts new Savings Account, one account holder.")
    void AccountController_PostSavingsAccount_Created_OneAccountHolder() throws Exception {
        SavingsAccountDTO testSavingsAccount2 = new SavingsAccountDTO(new Money(new BigDecimal(1500.00), Currency.getInstance("GBP")),accountHolder.getId(),"BABYMETAL",new Money(BigDecimal.valueOf(0.2),Currency.getInstance("GBP")));

        String body = objectMapper.writeValueAsString(testSavingsAccount2);

        MvcResult result = mockMvc.perform(
                post("/accounts/new/savings")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("1500.00"));
    }

    @Test
    @DisplayName("Test: POST new Savings Account. Posts new Savings Account, two account holders.")
    void AccountController_PostSavingsAccount_Created_TwoAccountHolders() throws Exception {
        SavingsAccountDTO testSavingsAccount2 = new SavingsAccountDTO(new Money(new BigDecimal(1500.00), Currency.getInstance("GBP")),accountHolder.getId(),accountHolder2.getId(),"BABYMETAL",new Money(BigDecimal.valueOf(0.2),Currency.getInstance("GBP")));

        String body = objectMapper.writeValueAsString(testSavingsAccount2);

        MvcResult result = mockMvc.perform(
                post("/accounts/new/savings")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("1500.00"));
    }

    @Test
    @DisplayName("Test: POST new Credit Card Account. Posts new Credit Card Account, one account holder")
    void AccountController_PostCreditCardAccount_Created_OneAccountHolder() throws Exception {
        CreditCardAccountDTO testCreditCardAccount2 = new CreditCardAccountDTO(new Money(new BigDecimal(425.00), Currency.getInstance("GBP")),accountHolder.getId(),new Money(BigDecimal.valueOf(500),Currency.getInstance("GBP")),new Money(BigDecimal.valueOf(0.2),Currency.getInstance("GBP")));

        String body = objectMapper.writeValueAsString(testCreditCardAccount2);

        MvcResult result = mockMvc.perform(
                post("/accounts/new/creditcard")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("425.00"));
    }

    @Test
    @DisplayName("Test: POST new Credit Card Account. Posts new Credit Card Account, two account holders.")
    void AccountController_PostCreditCardAccount_Created_TwoAccountHolders() throws Exception {
        CreditCardAccountDTO testCreditCardAccount2 = new CreditCardAccountDTO(new Money(new BigDecimal(425.00), Currency.getInstance("GBP")),accountHolder.getId(),accountHolder2.getId(),new Money(BigDecimal.valueOf(500),Currency.getInstance("GBP")),new Money(BigDecimal.valueOf(0.2),Currency.getInstance("GBP")));

        String body = objectMapper.writeValueAsString(testCreditCardAccount2);

        MvcResult result = mockMvc.perform(
                post("/accounts/new/creditcard")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("425.00"));
    }

    @Test
    @DisplayName("Test: POST new Third Party Account. Posts new Third Party Account, one account holder")
    void AccountController_PostThirdPartyAccount_Created_OneAccountHolder() throws Exception {
        ThirdPartyAccountDTO testThirdPartyAccount2 = new ThirdPartyAccountDTO(new Money(new BigDecimal(425.00), Currency.getInstance("GBP")),testThirdParty.getId(),"##hashedkey##","MortgageTime");

        String body = objectMapper.writeValueAsString(testThirdPartyAccount2);

        MvcResult result = mockMvc.perform(
                post("/accounts/new/thirdparty")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("425.00"));
    }

    @Test
    @DisplayName("Test: POST new Third Party Account. Posts new Third Party Account, two account holders")
    void AccountController_PostThirdPartyAccount_Created_TwoAccountHolders() throws Exception {
        ThirdPartyAccountDTO testThirdPartyAccount2 = new ThirdPartyAccountDTO(new Money(new BigDecimal(425.00), Currency.getInstance("GBP")),testThirdParty.getId(),testThirdParty2.getId(),"##hashedkey##","MortgageTime");

        String body = objectMapper.writeValueAsString(testThirdPartyAccount2);

        MvcResult result = mockMvc.perform(
                post("/accounts/new/thirdparty")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("425.00"));
    }
}

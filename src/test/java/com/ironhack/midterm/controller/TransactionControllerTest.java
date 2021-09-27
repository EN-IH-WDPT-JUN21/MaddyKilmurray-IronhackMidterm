package com.ironhack.midterm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ironhack.midterm.controller.dto.ThirdPartyTransactionDTO;
import com.ironhack.midterm.controller.dto.TransactionDTO;
import com.ironhack.midterm.dao.Address;
import com.ironhack.midterm.dao.Money;
import com.ironhack.midterm.dao.accounts.Account;
import com.ironhack.midterm.dao.accounts.accountsubclasses.*;
import com.ironhack.midterm.dao.users.Role;
import com.ironhack.midterm.dao.users.User;
import com.ironhack.midterm.dao.users.usersubclasses.AccountHolder;
import com.ironhack.midterm.dao.users.usersubclasses.Admin;
import com.ironhack.midterm.dao.users.usersubclasses.ThirdParty;
import com.ironhack.midterm.exceptions.BalanceOutOfBoundsException;
import com.ironhack.midterm.repository.accounts.AccountRepository;
import com.ironhack.midterm.repository.accounts.ThirdPartyAccountRepository;
import com.ironhack.midterm.repository.users.ThirdPartyRepository;
import com.ironhack.midterm.repository.users.UserRepository;
import org.hibernate.annotations.Check;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class TransactionControllerTest {

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
    private CheckingAccount testCheckingAccount;
    private StudentCheckingAccount testStudentCheckingAccount;
    private SavingsAccount testSavingsAccount;
    private CreditCardAccount testCreditCardAccount;
    private ThirdPartyAccount testThirdPartyAccount;

    private Address address1;
    private Address address2;

    @BeforeEach
    public void setUp() throws BalanceOutOfBoundsException {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        address1 = new Address(55,"Long Street","Manchester","M1 1AD","United Kingdom");
        address2 = new Address(2,"Short Avenue","Liverpool","L1 8JQ","United Kingdom");

        accountHolder = new AccountHolder("Ronda Grimes", "Ronda","gr1mes",
                new HashSet<Role>(),LocalDate.of(2005, Month.JANUARY, 8),address1,address2,
                new ArrayList<Account>());
        accountHolder2 = new AccountHolder("Barry White", "WhiteB","b@rry",
                new HashSet<Role>(),LocalDate.of(1960, Month.AUGUST, 30),address1,address2,
                new ArrayList<Account>());
        accountHolder3 = new AccountHolder("Will Smith", "WSmith","sm1th",
                new HashSet<Role>(),LocalDate.of(1980, Month.MARCH, 16),address1,address2,
                new ArrayList<Account>());
        testThirdParty = new ThirdParty("Will Smith", "WSmith","sm1th",
                new HashSet<Role>(),"###hashedkey###");
        userRepository.save(accountHolder);
        userRepository.save(accountHolder2);
        userRepository.save(accountHolder3);
        userRepository.save(testThirdParty);

        testCheckingAccount = new CheckingAccount(new Money(new BigDecimal(55.65),Currency.getInstance("GBP")),accountHolder,"BANANA");
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
        thirdPartyAccountRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Test: GET checking balance by id. Return the checking account balance based on id")
    public void TransactionController_GetCheckingBalanceById_ReturnAsExpected() throws Exception {
        BigDecimal testValue = new BigDecimal(55.65);

        MvcResult mvcResult = mockMvc.perform( MockMvcRequestBuilders.get("/accounts/getbalance/checking/" + testCheckingAccount.getId())
                        .param("username","Ronda"))
                .andExpect(status().isOk()).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("55"));
    }

    @Test
    @DisplayName("Test: GET checking balance by id. Balance not returned as user doesn't exist.")
    void TransactionController_GetCheckingBalanceById_UserDoesNotExist() throws Exception {

        ResultActions mvcResult = mockMvc.perform( MockMvcRequestBuilders.get("/accounts/getbalance/checking/" + 999)
                        .param("username","Ronda"))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));
    }

    @Test
    @DisplayName("Test: GET checking balance by id. Balance not returned as username doesn't match.")
    void TransactionController_GetCheckingBalanceById_UserNameDoesNotMatch() throws Exception {

        ResultActions mvcResult = mockMvc.perform( MockMvcRequestBuilders.get("/accounts/getbalance/checking/" + testCheckingAccount.getId())
                        .param("username","NotRonda"))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));
    }

    @Test
    @DisplayName("Test: GET student balance by id. Return the student account balance based on id")
    public void TransactionController_GetStudentBalanceById_ReturnAsExpected() throws Exception {
        BigDecimal testValue = new BigDecimal(66.23);

        MvcResult mvcResult = mockMvc.perform( MockMvcRequestBuilders.get("/accounts/getbalance/studentchecking/" + testStudentCheckingAccount.getId())
                        .param("username","Ronda"))
                .andExpect(status().isOk()).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("66"));
    }

    @Test
    @DisplayName("Test: GET student balance by id. Balance not returned as user doesn't exist.")
    void TransactionController_GetStudentBalanceById_UserDoesNotExist() throws Exception {

        ResultActions mvcResult = mockMvc.perform( MockMvcRequestBuilders.get("/accounts/getbalance/checking/" + 999)
                        .param("username","Ronda"))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));
    }

    @Test
    @DisplayName("Test: GET student balance by id. Balance not returned as username doesn't match.")
    void TransactionController_GetStudentBalanceById_UserNameDoesNotMatch() throws Exception {

        ResultActions mvcResult = mockMvc.perform( MockMvcRequestBuilders.get("/accounts/getbalance/checking/" + testCheckingAccount.getId())
                        .param("username","NotRonda"))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));
    }

    @Test
    @DisplayName("Test: GET savings balance by id. Return the savings account balance based on id")
    public void TransactionController_GetSavingsBalanceById_ReturnAsExpected() throws Exception {
        MvcResult mvcResult = mockMvc.perform( MockMvcRequestBuilders.get("/accounts/getbalance/savings/" + testSavingsAccount.getId())
                        .param("username","WhiteB"))
                .andExpect(status().isOk()).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("101.00"));
    }

    @Test
    @DisplayName("Test: GET savings balance by id. Balance not returned as user doesn't exist.")
    void TransactionController_GetSavingsBalanceById_UserDoesNotExist() throws Exception {

        ResultActions mvcResult = mockMvc.perform( MockMvcRequestBuilders.get("/accounts/getbalance/savings/" + 999)
                        .param("username","WhiteB"))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));
    }

    @Test
    @DisplayName("Test: GET savings balance by id. Balance not returned as username doesn't match.")
    void TransactionController_GetSavingsBalanceById_UserNameDoesNotMatch() throws Exception {

        ResultActions mvcResult = mockMvc.perform( MockMvcRequestBuilders.get("/accounts/getbalance/savings/" + testSavingsAccount.getId())
                        .param("username","NotWhiteB"))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));
    }

    @Test
    @DisplayName("Test: GET credit card balance by id. Return the credit card account balance based on id")
    public void TransactionController_GetCreditCardBalanceById_ReturnAsExpected() throws Exception {
        MvcResult mvcResult = mockMvc.perform( MockMvcRequestBuilders.get("/accounts/getbalance/creditcard/" + testCreditCardAccount.getId())
                        .param("username","WSmith"))
                .andExpect(status().isOk()).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("489"));
    }

    @Test
    @DisplayName("Test: GET credit card balance by id. Balance not returned as user doesn't exist.")
    void TransactionController_GetCreditCardBalanceById_UserDoesNotExist() throws Exception {

        ResultActions mvcResult = mockMvc.perform( MockMvcRequestBuilders.get("/accounts/getbalance/creditcard/" + 999)
                        .param("username","WSmith"))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));
    }

    @Test
    @DisplayName("Test: GET credit card balance by id. Balance not returned as username doesn't match.")
    void TransactionController_GetCreditCardBalanceById_UserNameDoesNotMatch() throws Exception {

        ResultActions mvcResult = mockMvc.perform( MockMvcRequestBuilders.get("/accounts/getbalance/savings/" + testCreditCardAccount.getId())
                        .param("username","NotWSmith"))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));
    }

    @Test
    @DisplayName("Test: GET third party balance by id. Return the third party account balance based on id")
    public void TransactionController_GetThirdPartyBalanceById_ReturnAsExpected() throws Exception {
        MvcResult mvcResult = mockMvc.perform( MockMvcRequestBuilders.get("/accounts/getbalance/thirdparty/" + testThirdPartyAccount.getId())
                        .param("username","WSmith"))
                .andExpect(status().isOk()).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("2154"));
    }

    @Test
    @DisplayName("Test: GET third party balance by id. Balance not returned as user doesn't exist.")
    void TransactionController_GetThirdPartyBalanceById_UserDoesNotExist() throws Exception {

        ResultActions mvcResult = mockMvc.perform( MockMvcRequestBuilders.get("/accounts/getbalance/thirdparty/" + 999)
                        .param("username","WSmith"))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));
    }

    @Test
    @DisplayName("Test: GET third party balance by id. Balance not returned as username doesn't match.")
    void TransactionController_GetThirdPartyBalanceById_UserNameDoesNotMatch() throws Exception {

        ResultActions mvcResult = mockMvc.perform( MockMvcRequestBuilders.get("/accounts/getbalance/thirdparty/" + testThirdPartyAccount.getId())
                        .param("username","NotWSmith"))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));
    }

    @Test
    @DisplayName("Test: PATCH transfer funds, admin route. Transfers funds between accounts")
    void TransactionController_PatchAdminTransferFunds_Updated() throws Exception {
        TransactionDTO newTransaction = new TransactionDTO(BigDecimal.valueOf(50.00),testCheckingAccount.getId(),
                testSavingsAccount.getId());

        String body = objectMapper.registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .writeValueAsString(newTransaction);

        MvcResult result = mockMvc.perform(patch("/accounts/admin/transferfunds/")
                        .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("" + testCheckingAccount.getId()));
    }

    @Test
    @DisplayName("Test: PATCH transfer funds, admin route. Penalty Fee applied, transfer too much")
    void TransactionController_PatchAdminTransferFunds_PenaltyFeeApplied() throws Exception {
        BigDecimal balanceBeforeTransaction = testCheckingAccount.getBalance().getAmount();
        TransactionDTO newTransaction = new TransactionDTO(BigDecimal.valueOf(500000.00),testCheckingAccount.getId(),
                testSavingsAccount.getId());

        String body = objectMapper.registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .writeValueAsString(newTransaction);

        ResultActions mvcResult = mockMvc.perform(patch("/accounts/admin/transferfunds/")
                        .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));

        Optional<Account> foundAccount = accountRepository.findById(testCheckingAccount.getId());
        assertEquals(foundAccount.get().getBalance().getAmount(),balanceBeforeTransaction.subtract(BigDecimal.valueOf(40)));
    }

    @Test
    @DisplayName("Test: PATCH transfer funds, admin route. Fraud found, multiple transactions")
    void TransactionController_PatchAdminTransferFunds_FraudFound_TooManyTransactions() throws Exception {
        TransactionDTO newTransaction = new TransactionDTO(BigDecimal.valueOf(5.00),testCheckingAccount.getId(),
                testSavingsAccount.getId());

        String body = objectMapper.registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .writeValueAsString(newTransaction);

        MvcResult result1 = mockMvc.perform(patch("/accounts/admin/transferfunds/")
                        .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andReturn();

        MvcResult result2 = mockMvc.perform(patch("/accounts/admin/transferfunds/")
                        .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andReturn();

        ResultActions mvcResult = mockMvc.perform(patch("/accounts/admin/transferfunds/")
                        .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));
    }

    @Test
    @DisplayName("Test: PATCH transfer funds, accountholder route. Transfers funds between accounts")
    void TransactionController_PatchAccountHolderTransferFunds_Updated() throws Exception {
        TransactionDTO newTransaction = new TransactionDTO(BigDecimal.valueOf(50.00),testCheckingAccount.getId(),
                testSavingsAccount.getId());

        String body = objectMapper.registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .writeValueAsString(newTransaction);

        MvcResult result = mockMvc.perform(patch("/accounts/accountholder/transferfunds/" + accountHolder.getUsername())
                        .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("" + testCheckingAccount.getId()));
    }

    @Test
    @DisplayName("Test: PATCH transfer funds, accountholder route. Penalty Fee applied, transfer too much")
    void TransactionController_PatchAccountHolderTransferFunds_PenaltyFeeApplied() throws Exception {
        BigDecimal balanceBeforeTransaction = testCheckingAccount.getBalance().getAmount();
        TransactionDTO newTransaction = new TransactionDTO(BigDecimal.valueOf(500000.00),testCheckingAccount.getId(),
                testSavingsAccount.getId());

        String body = objectMapper.registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .writeValueAsString(newTransaction);

        ResultActions mvcResult = mockMvc.perform(patch("/accounts/accountholder/transferfunds/" + accountHolder.getUsername())
                        .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));

        Optional<Account> foundAccount = accountRepository.findById(testCheckingAccount.getId());
        assertEquals(foundAccount.get().getBalance().getAmount(),balanceBeforeTransaction.subtract(BigDecimal.valueOf(40)));
    }

    @Test
    @DisplayName("Test: PATCH transfer funds, accountholder route. Fraud found, multiple transactions")
    void TransactionController_PatchAccountHolderTransferFunds_FraudFound_TooManyTransactions() throws Exception {
        TransactionDTO newTransaction = new TransactionDTO(BigDecimal.valueOf(5.00),testCheckingAccount.getId(),
                testSavingsAccount.getId());

        String body = objectMapper.registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .writeValueAsString(newTransaction);

        MvcResult result1 = mockMvc.perform(patch("/accounts/accountholder/transferfunds/" + accountHolder.getUsername())
                        .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andReturn();

        MvcResult result2 = mockMvc.perform(patch("/accounts/accountholder/transferfunds/" + accountHolder.getUsername())
                        .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andReturn();

        ResultActions mvcResult = mockMvc.perform(patch("/accounts/accountholder/transferfunds/" + accountHolder.getUsername())
                        .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));
    }

    @Test
    @DisplayName("Test: PATCH transfer funds, third party route. Transfers funds between accounts")
    void TransactionController_PatchThirdPartyTransferFunds_Updated() throws Exception {
        ThirdPartyTransactionDTO newTransaction = new ThirdPartyTransactionDTO(BigDecimal.valueOf(50.00),testThirdPartyAccount.getId(),
                testSavingsAccount.getId(),"CHERRY");

        String body = objectMapper.registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .writeValueAsString(newTransaction);

        MvcResult result = mockMvc.perform(patch("/accounts/thirdparty/transferfunds/")
                        .param("hashedKey","###hashedkey###")
                        .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("" + testThirdPartyAccount.getId()));
    }

    @Test
    @DisplayName("Test: PATCH transfer funds, third party route. Penalty Fee applied, transfer too much")
    void TransactionController_PatchThirdPartyTransferFunds_PenaltyFeeApplied() throws Exception {
        BigDecimal balanceBeforeTransaction = testThirdPartyAccount.getBalance().getAmount();
        ThirdPartyTransactionDTO newTransaction = new ThirdPartyTransactionDTO(BigDecimal.valueOf(50000.00),testThirdPartyAccount.getId(),
                testSavingsAccount.getId(),"CHERRY");

        String body = objectMapper.registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .writeValueAsString(newTransaction);

        ResultActions mvcResult = mockMvc.perform(patch("/accounts/thirdparty/transferfunds/")
                        .param("hashedKey","###hashedkey###")
                        .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));

        Optional<ThirdPartyAccount> foundAccount = thirdPartyAccountRepository.findById(testThirdPartyAccount.getId());
        assertEquals(foundAccount.get().getBalance().getAmount(),balanceBeforeTransaction.subtract(BigDecimal.valueOf(40)));
    }

    @Test
    @DisplayName("Test: PATCH transfer funds, third party route. Fraud found, multiple transactions")
    void TransactionController_PatchThirdPartyTransferFunds_FraudFound_TooManyTransactions() throws Exception {
        ThirdPartyTransactionDTO newTransaction = new ThirdPartyTransactionDTO(BigDecimal.valueOf(5.00),testThirdPartyAccount.getId(),
                testSavingsAccount.getId(),"CHERRY");

        String body = objectMapper.registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .writeValueAsString(newTransaction);

        MvcResult result1 = mockMvc.perform(patch("/accounts/thirdparty/transferfunds/")
                        .param("hashedKey","###hashedkey###")
                        .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andReturn();

        MvcResult result2 = mockMvc.perform(patch("/accounts/thirdparty/transferfunds/")
                        .param("hashedKey","###hashedkey###")
                        .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andReturn();

        ResultActions mvcResult = mockMvc.perform(patch("/accounts/thirdparty/transferfunds/")
                        .param("hashedKey","###hashedkey###")
                        .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));
    }
}

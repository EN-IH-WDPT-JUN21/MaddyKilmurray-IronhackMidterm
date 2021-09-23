package com.ironhack.midterm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midterm.dao.Address;
import com.ironhack.midterm.dao.Money;
import com.ironhack.midterm.dao.accounts.Account;
import com.ironhack.midterm.dao.users.Role;
import com.ironhack.midterm.dao.users.usersubclasses.AccountHolder;
import com.ironhack.midterm.dao.users.usersubclasses.Admin;
import com.ironhack.midterm.dao.users.usersubclasses.ThirdParty;
import com.ironhack.midterm.repository.AccountRepository;
import com.ironhack.midterm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class TransactionControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Admin testAdmin = null;
    private AccountHolder testAccountHolder1 = null;
    private AccountHolder testAccountHolder2 = null;
    private ThirdParty testThirdParty = null;

    private Account testAccount = null;
    private List<Account> testAccountList = null;

    private Address address1;
    private Address address2;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        testAccount = new Account(new Money(BigDecimal.valueOf(55.65), Currency.getInstance("GBP")));
        testAccountList.add(testAccount);
        accountRepository.save(testAccount);

        address1 = new Address(55,"Long Street","Manchester","M1 1AD","United Kingdom");
        address2 = new Address(2,"Short Avenue","Liverpool","L1 8JQ","United Kingdom");

        testAdmin = new Admin("Linda Ronstadt","Ronstadt","lind@",new HashSet<Role>());
        testAccountHolder1 = new AccountHolder("Ronda Grimes", "Ronda","gr1mes",
                new HashSet<Role>(), LocalDate.of(2005, Month.JANUARY, 8),address1,address2,
                new ArrayList<Account>());
        testAccountHolder2 = new AccountHolder("Melissa McCarthy", "McCarthy","melmel",
                new HashSet<Role>(),LocalDate.of(1970, Month.AUGUST, 26),address2,address1,
                testAccountList);
        testThirdParty = new ThirdParty("Bust-A-Mortgage","mortgage","bust@",new HashSet<Role>(),
                "banana");
        userRepository.save(testAdmin);
        userRepository.save(testAccountHolder1);
        userRepository.save(testAccountHolder2);
        userRepository.save(testThirdParty);
    }

    @Test
    @DisplayName("GET balance by id. Return the account balance based on id")
    public void TransactionController_GetBalanceById_ReturnAsExpected() throws Exception {
        BigDecimal testValue = new BigDecimal(55.65);

        MvcResult mvcResult = mockMvc.perform( MockMvcRequestBuilders.get("/accounts/getbalance/" + testAccount.getId()))
                .andExpect(status().isOk()).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains(testValue.toString()));
    }
}

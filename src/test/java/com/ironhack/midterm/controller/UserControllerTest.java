package com.ironhack.midterm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midterm.dao.Address;
import com.ironhack.midterm.dao.accounts.Account;
import com.ironhack.midterm.dao.users.Role;
import com.ironhack.midterm.dao.users.User;
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

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class UserControllerTest {

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

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        Address address1 = new Address(55,"Long Street","Manchester","M1 1AD","United Kingdom");
        Address address2 = new Address(2,"Short Avenue","Liverpool","L1 8JQ","United Kingdom");

        testAdmin = new Admin("Linda Ronstadt","Ronstadt","lind@",new HashSet<Role>());
        testAccountHolder1 = new AccountHolder("Ronda Grimes", "Ronda","gr1mes",
                new HashSet<Role>(),LocalDate.of(2005, Month.JANUARY, 8),address1,address2,
                new ArrayList<Account>());
        testAccountHolder2 = new AccountHolder("Melissa McCarthy", "McCarthy","melmel",
                new HashSet<Role>(),LocalDate.of(1970, Month.AUGUST, 26),address2,address1,
                new ArrayList<Account>());
        testThirdParty = new ThirdParty("Bust-A-Mortgage","mortgage","bust@",new HashSet<Role>(),
                "banana");
        userRepository.save(testAdmin);
        userRepository.save(testAccountHolder1);
        userRepository.save(testAccountHolder2);
        userRepository.save(testThirdParty);
    }

    @Test
    @DisplayName("Test: GET users. Returns all users as expected")
    void UserController_GetAllUsers_AsExpected() throws Exception{
        MvcResult mvcResult = mockMvc.perform( MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk()).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Melissa McCarthy"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Linda Ronstadt"));
    }

    @Test
    @DisplayName("Test: GET user by username. Returns all user as expected")
    void UserController_GetUserByUsername_AsExpected() throws Exception{
        MvcResult mvcResult = mockMvc.perform( MockMvcRequestBuilders.get("/users/" + testAccountHolder1.getUsername()))
                .andExpect(status().isOk()).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Ronda Grimes"));
    }


}

package com.ironhack.midterm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ironhack.midterm.dao.Address;
import com.ironhack.midterm.dao.Money;
import com.ironhack.midterm.dao.accounts.Account;
import com.ironhack.midterm.dao.users.Role;
import com.ironhack.midterm.dao.users.usersubclasses.AccountHolder;
import com.ironhack.midterm.dao.users.usersubclasses.Admin;
import com.ironhack.midterm.dao.users.usersubclasses.ThirdParty;
import com.ironhack.midterm.repository.accounts.AccountRepository;
import com.ironhack.midterm.repository.users.UserRepository;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    private Account testAccount = null;
    private List<Account> testAccountList = null;

    private Address address1;
    private Address address2;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        address1 = new Address(55,"Long Street","Manchester","M1 1AD","United Kingdom");
        address2 = new Address(2,"Short Avenue","Liverpool","L1 8JQ","United Kingdom");

        testAdmin = new Admin("Linda Ronstadt","Ronstadt","lind@",new HashSet<Role>());
        testAccountHolder1 = new AccountHolder("Ronda Grimes", "Ronda","gr1mes",
                new HashSet<Role>(),LocalDate.of(2005, Month.JANUARY, 8),address1,address2,
                new ArrayList<Account>());
        testAccountHolder2 = new AccountHolder("Melissa McCarthy", "McCarthy","melmel",
                new HashSet<Role>(),LocalDate.of(1970, Month.AUGUST, 26),address2,address1,
                new ArrayList<>());
        testThirdParty = new ThirdParty("Bust-A-Mortgage","mortgage","bust@",new HashSet<Role>(),
                "banana");
        userRepository.save(testAdmin);
        userRepository.save(testAccountHolder1);
        userRepository.save(testAccountHolder2);
        userRepository.save(testThirdParty);

        testAccount = new Account(new Money(BigDecimal.valueOf(55.65), Currency.getInstance("GBP")));
        accountRepository.save(testAccount);
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
    @DisplayName("Test: GET user by username. Returns user as expected")
    void UserController_GetUserByUsername_AsExpected() throws Exception{
        MvcResult mvcResult = mockMvc.perform( MockMvcRequestBuilders.get("/users/" + testAccountHolder1.getUsername()))
                .andExpect(status().isOk()).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Ronda"));
    }

    @Test
    @DisplayName("Test: GET user by id. Returns user as expected")
    void UserController_GetUserById_AsExpected() throws Exception{
        MvcResult mvcResult = mockMvc.perform( MockMvcRequestBuilders.get("/users/byid/" + testAccountHolder1.getId()))
                .andExpect(status().isOk()).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Ronda Grimes"));
    }

    @Test
    @DisplayName("Test: POST new Admin. Posts new Admin")
    void UserController_PostAdmin_Created() throws Exception {
        Admin testAdmin2 = new Admin("Mr Smith","DownWithNeo","sm1th",new HashSet<Role>());

        String body = objectMapper.writeValueAsString(testAdmin2);

        MvcResult result = mockMvc.perform(
                post("/users/new/admin")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("DownWithNeo"));
    }

    @Test
    @DisplayName("Test: POST new AccountHolder. Posts new AccountHolder")
    void UserController_PostAccountHolder_Created() throws Exception {
        AccountHolder testHolder = new AccountHolder("Mr Smith","DownWithNeo","sm1th",new HashSet<Role>(),
                LocalDate.of(1970, 8, 26),address1,address2,new ArrayList<Account>());

        String body = objectMapper.registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .writeValueAsString(testHolder);

        MvcResult result = mockMvc.perform(
                post("/users/new/accountholder")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("1970-08-26"));
    }

    @Test
    @DisplayName("Test: POST new ThirdParty. Posts new ThirdParty")
    void UserController_PostThirdParty_Created() throws Exception {
        ThirdParty testHolder = new ThirdParty("Mr Smith","DownWithNeo","sm1th",
                new HashSet<Role>(),"##hashed##");

        String body = objectMapper.writeValueAsString(testHolder);

        MvcResult result = mockMvc.perform(
                post("/users/new/thirdparty")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("##hashed##"));
    }

    @Test
    @DisplayName("Test: PATCH Username and Password. Updates an existing Account with new username and password")
    void UserController_PatchUsernameAndPassword_Updated() throws Exception {
        String username = "NewUsername";
        String password = "SuperSecurePassword";

        MvcResult result = mockMvc.perform(patch("/users/update/logindetails/" + testAccountHolder1.getId())
                        .param("username",username)
                        .param("password",password))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    @DisplayName("Test: PATCH Account Holder. Updates Existing Account Holder")
    void UserController_PatchAccountHolder_Updated() throws Exception {
        AccountHolder testHolder = new AccountHolder("",null,null,null,
                LocalDate.of(2002, 8, 15),address1,null,null);

        String body = objectMapper.registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .writeValueAsString(testHolder);

        MvcResult result = mockMvc.perform(patch("/users/update/accountholder/" + testAccountHolder1.getId())
                        .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("2002-08-15"));
    }

    @Test
    @DisplayName("Test: PATCH Third Party. Updates Existing Third Party")
    void UserController_PatchThirdParty_Updated() throws Exception {
        ThirdParty testHolder = new ThirdParty("neo","TakeTheBluePill",null,
                null,"bluepill");

        String body = objectMapper.writeValueAsString(testHolder);

        MvcResult result = mockMvc.perform(patch("/users/update/thirdparty/" + testThirdParty.getId())
                        .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("TakeTheBluePill"));
    }

    @Test
    @DisplayName("Test: PATCH admin. Updates Existing admin")
    void UserController_PatchAdmin_Updated() throws Exception {
        Admin testAdmin2 = new Admin("","LindaR",null,null);

        String body = objectMapper.writeValueAsString(testAdmin2);

        MvcResult result = mockMvc.perform(patch("/users/update/admin/" + testAdmin.getId())
                        .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("LindaR"));
    }

    @Test
    @DisplayName("Test: DELETE user. Removes a user.")
    void UserController_DeleteUser_Removed() throws Exception {
        var countBeforeRequest = userRepository.count();
        MvcResult result = mockMvc.perform( MockMvcRequestBuilders
                .delete("/users/remove/" + testAccountHolder1.getId())).andExpect(status().isAccepted()).andReturn();
        var countAfterRequest = userRepository.count();

        assertEquals(countAfterRequest,countBeforeRequest - 1);
    }
}

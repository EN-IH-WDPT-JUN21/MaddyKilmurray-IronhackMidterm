package com.ironhack.midterm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ironhack.midterm.controller.dto.users.AccountHolderDTO;
import com.ironhack.midterm.controller.dto.users.AdminDTO;
import com.ironhack.midterm.controller.dto.users.ThirdPartyDTO;
import com.ironhack.midterm.dao.Address;
import com.ironhack.midterm.dao.Money;
import com.ironhack.midterm.dao.accounts.Account;
import com.ironhack.midterm.dao.users.Role;
import com.ironhack.midterm.dao.users.usersubclasses.AccountHolder;
import com.ironhack.midterm.dao.users.usersubclasses.Admin;
import com.ironhack.midterm.dao.users.usersubclasses.ThirdParty;
import com.ironhack.midterm.repository.accounts.AccountRepository;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;

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

        testAdmin = new Admin("Linda Ronstadt","Ronstadt","lind@");
        testAccountHolder1 = new AccountHolder("Ronda Grimes", "Ronda","gr1mes",
                LocalDate.of(2005, Month.JANUARY, 8),address1,address2,
                new ArrayList<Account>());
        testAccountHolder2 = new AccountHolder("Melissa McCarthy", "McCarthy","melmel",
                LocalDate.of(1970, Month.AUGUST, 26),address2,address1,
                new ArrayList<>());
        testThirdParty = new ThirdParty("Bust-A-Mortgage","mortgage","bust@",
                "banana");
        userRepository.save(testAdmin);
        userRepository.save(testAccountHolder1);
        userRepository.save(testAccountHolder2);
        userRepository.save(testThirdParty);

        testAccount = new Account(new Money(BigDecimal.valueOf(55.65), Currency.getInstance("GBP")));
        accountRepository.save(testAccount);
    }

    @AfterEach
    public void tearDown() {
        accountRepository.deleteAll();
        userRepository.deleteAll();
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
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Ronda Grimes"));
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
        AdminDTO testAdmin2 = new AdminDTO("Mr Smith","DownWithNeo","sm1th");

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
        AccountHolderDTO testHolder = new AccountHolderDTO("Mr Smith","DownWithNeo","sm1th",
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
        ThirdPartyDTO testHolder = new ThirdPartyDTO("Mr Smith","DownWithNeo","sm1th",
                "##hashed##");

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
    @DisplayName("Test: PATCH Username and Password. No user exists so no change.")
    void UserController_PatchUsernameAndPassword_NotUpdated() throws Exception {
        String username = "NewUsername";
        String password = "SuperSecurePassword";

        ResultActions mvcResult = mockMvc.perform(patch("/users/update/logindetails/" + 999)
                        .param("username",username)
                        .param("password",password))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));
    }

    @Test
    @DisplayName("Test: PATCH Username. Updates an existing Account with a new username")
    void UserController_PatchUsername_Updated() throws Exception {
        String username = "NewUsername";

        MvcResult result = mockMvc.perform(patch("/users/update/username/" + testAccountHolder1.getId())
                        .param("username",username))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    @DisplayName("Test: PATCH Username. No user exists so no change.")
    void UserController_PatchUsername_NotUpdated() throws Exception {
        String username = "NewUsername";

        ResultActions mvcResult = mockMvc.perform(patch("/users/update/username/" + 999)
                        .param("username",username))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));
    }

    @Test
    @DisplayName("Test: PATCH Password. Updates an existing Account with a new password")
    void UserController_PatchPassword_Updated() throws Exception {
        String password = "SuperSecurePassword";

        MvcResult result = mockMvc.perform(patch("/users/update/password/" + testAccountHolder1.getId())
                        .param("password",password))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    @DisplayName("Test: PATCH Password. No user exists so no change.")
    void UserController_PatchPassword_NotUpdated() throws Exception {
        String password = "SuperSecurePassword";

        ResultActions mvcResult = mockMvc.perform(patch("/users/update/password/" + 999)
                        .param("password",password))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));
    }

    @Test
    @DisplayName("Test: PATCH Account Holder. Updates Existing Account Holder")
    void UserController_PatchAccountHolder_Updated() throws Exception {
        AccountHolderDTO testHolder = new AccountHolderDTO("",null,null,
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
        ThirdPartyDTO testHolder = new ThirdPartyDTO("neo","TakeTheBluePill",
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
        AdminDTO testAdmin2 = new AdminDTO("","LindaR",null);

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

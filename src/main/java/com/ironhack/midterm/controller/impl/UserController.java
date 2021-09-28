package com.ironhack.midterm.controller.impl;

import com.ironhack.midterm.controller.dto.users.AccountHolderDTO;
import com.ironhack.midterm.controller.dto.users.AdminDTO;
import com.ironhack.midterm.controller.dto.users.ThirdPartyDTO;
import com.ironhack.midterm.controller.dto.users.UserDTO;
import com.ironhack.midterm.dao.users.User;
import com.ironhack.midterm.repository.accounts.AccountRepository;
import com.ironhack.midterm.repository.users.UserRepository;
import com.ironhack.midterm.service.interfaces.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private IUserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{username}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getAllUsersByUsername(@PathVariable(name = "username") String name) {
        return convertToDto(userRepository.findByUsername(name).get());
    }

    @GetMapping("/users/byid/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<UserDTO> getAllUsersById(@PathVariable(name = "id") Long id) {
        return Optional.ofNullable(convertToDto(userRepository.findById(id).get()));
    }

    @PostMapping("/users/new/admin")
    @ResponseStatus(HttpStatus.CREATED)
    public AdminDTO createNewAdmin(@RequestBody @Valid AdminDTO admin) {
        return userService.createNewAdmin(admin);
    }

    @PostMapping("/users/new/accountholder")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountHolderDTO createNewAccountHolder(@RequestBody @Valid AccountHolderDTO accountHolder) {
        return userService.createNewAccountHolder(accountHolder);
    }

    @PostMapping("/users/new/thirdparty")
    @ResponseStatus(HttpStatus.CREATED)
    public ThirdPartyDTO createNewThirdParty(@RequestBody @Valid ThirdPartyDTO thirdParty) {
        return userService.createNewThirdParty(thirdParty);
    }

    @PatchMapping("/users/update/logindetails/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateUsernameAndPassword(@PathVariable(name = "id") Long id,
                                          @RequestParam String username,
                                          @RequestParam String password) {
        userService.updateUsernameAndPassword(id,username,password);
    }

    @PatchMapping("/users/update/username/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateUsername(@PathVariable(name = "id") Long id,
                                          @RequestParam String username) {
        userService.updateUsername(id,username);
    }

    @PatchMapping("/users/update/password/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updatePassword(@PathVariable(name = "id") Long id,
                                          @RequestParam String password) {
        userService.updatePassword(id,password);
    }

    @PatchMapping("/users/update/accountholder/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AccountHolderDTO updateUser(@PathVariable(name = "id") Long id,
                               @RequestBody @Valid AccountHolderDTO accountHolder) {
        return userService.updateHolder(id,accountHolder);
    }

    @PatchMapping("/users/update/thirdparty/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ThirdPartyDTO updateUser(@PathVariable(name = "id") Long id,
                           @RequestBody @Valid ThirdPartyDTO thirdParty) {
        return userService.updateThirdParty(id,thirdParty);
    }

    @PatchMapping("/users/update/admin/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AdminDTO updateUser(@PathVariable(name = "id") Long id,
                           @RequestBody @Valid AdminDTO admin) {
        return userService.updateAdmin(id,admin);
    }

    @DeleteMapping("/users/remove/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void removeUser(@PathVariable(name = "id") Long id) {
        userService.remove(id);
    }

    private UserDTO convertToDto(User user) {
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return userDTO;
    }
}

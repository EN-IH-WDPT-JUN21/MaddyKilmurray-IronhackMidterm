package com.ironhack.midterm.service.impl;

import com.ironhack.midterm.controller.dto.users.AccountHolderDTO;
import com.ironhack.midterm.controller.dto.users.AdminDTO;
import com.ironhack.midterm.controller.dto.users.ThirdPartyDTO;
import com.ironhack.midterm.dao.users.Role;
import com.ironhack.midterm.dao.users.User;
import com.ironhack.midterm.dao.users.usersubclasses.AccountHolder;
import com.ironhack.midterm.dao.users.usersubclasses.Admin;
import com.ironhack.midterm.dao.users.usersubclasses.ThirdParty;
import com.ironhack.midterm.repository.accounts.AccountRepository;
import com.ironhack.midterm.repository.users.AccountHolderRepository;
import com.ironhack.midterm.repository.users.AdminRepository;
import com.ironhack.midterm.repository.users.ThirdPartyRepository;
import com.ironhack.midterm.repository.users.UserRepository;
import com.ironhack.midterm.service.interfaces.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    @Autowired
    private ModelMapper modelMapper;

    public AdminDTO createNewAdmin(AdminDTO admin) {
        Admin newUser = new Admin(admin.getName(), admin.getUsername(), admin.getPassword());
        userRepository.save(newUser);
        return convertToAdminDto(newUser);
    }

    public AccountHolderDTO createNewAccountHolder(AccountHolderDTO accountHolder) {
        AccountHolder newUser = new AccountHolder(accountHolder.getName(), accountHolder.getUsername(),
                    accountHolder.getPassword(), accountHolder.getDateOfBirth(), accountHolder.getPrimaryAddress(),
                    accountHolder.getMailingAddress(), accountHolder.getAccounts());
        userRepository.save(newUser);
        return convertToAccHolderDto(newUser);
    }

    public ThirdPartyDTO createNewThirdParty(ThirdPartyDTO thirdParty) {
        ThirdParty newUser = new ThirdParty(thirdParty.getName(), thirdParty.getUsername(), thirdParty.getPassword(), thirdParty.getHashedKey());
        userRepository.save(newUser);
        return convertToThirdPartyDto(newUser);
    }

    public void updateUsernameAndPassword(Long id,String username, String password) {
        Optional<User> foundUser = userRepository.findById(id);
        if (!foundUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User doesn't exist");
        }
        foundUser.get().setUsername(username);
        foundUser.get().setPassword(password);
        userRepository.save(foundUser.get());
    }

    public void updateUsername(Long id,String username) {
        Optional<User> foundUser = userRepository.findById(id);
        if (!foundUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User doesn't exist");
        }
        foundUser.get().setUsername(username);
        userRepository.save(foundUser.get());
    }

    public void updatePassword(Long id,String password) {
        Optional<User> foundUser = userRepository.findById(id);
        if (!foundUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User doesn't exist");
        }
        foundUser.get().setPassword(password);
        userRepository.save(foundUser.get());
    }

    public AccountHolderDTO updateHolder(Long id,AccountHolderDTO accountHolder) {
        Optional<AccountHolder> foundUser = accountHolderRepository.findById(id);
        if (!foundUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Account holder doesn't exist.");
        }
        if (!accountHolder.getName().isBlank() || !accountHolder.getName().equals(" ")) {
            foundUser.get().setName(accountHolder.getName());
        }
        if (accountHolder.getUsername()!=null) {
            foundUser.get().setUsername(accountHolder.getUsername());
        }
        if (accountHolder.getPassword() != null) {
            foundUser.get().setPassword(accountHolder.getPassword());
        }
        if (accountHolder.getAccounts()!=null) {
            (foundUser.get()).setAccounts(accountHolder.getAccounts());
        }
        if (accountHolder.getDateOfBirth() != null) {
            (foundUser.get()).setDateOfBirth(accountHolder.getDateOfBirth());
        }
        if (accountHolder.getMailingAddress() != null) {
            (foundUser.get()).setMailingAddress(accountHolder.getMailingAddress());
        }
        if (accountHolder.getPrimaryAddress() != null) {
            (foundUser.get()).setPrimaryAddress(accountHolder.getPrimaryAddress());
        }
        accountHolderRepository.save(foundUser.get());
        return convertToAccHolderDto(foundUser.get());
    }

    public ThirdPartyDTO updateThirdParty(Long id,ThirdPartyDTO thirdParty) {
        Optional<ThirdParty> foundUser = thirdPartyRepository.findById(id);
        if (!foundUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Third Party user doesn't exist.");
        }
        if (thirdParty.getName().isBlank() || !thirdParty.getName().equals(" ")) {
            foundUser.get().setName(thirdParty.getName());
        }
        if (thirdParty.getUsername()!=null) {
            foundUser.get().setUsername(thirdParty.getUsername());
        }
        if (thirdParty.getPassword() != null) {
            foundUser.get().setPassword(thirdParty.getPassword());
        }
        if (!thirdParty.getHashedKey().isBlank() || !thirdParty.getHashedKey().equals(" ")) {
            foundUser.get().setHashedKey(thirdParty.getHashedKey());
        }
        thirdPartyRepository.save(foundUser.get());
        return convertToThirdPartyDto(foundUser.get());
    }

    public AdminDTO updateAdmin(Long id,AdminDTO admin) {
        Optional<Admin> foundUser = adminRepository.findById(id);
        if (!foundUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Admin doesn't exist.");
        }
        if (!admin.getName().isBlank() || !admin.getName().equals(" ")) {
            foundUser.get().setName(admin.getName());
        }
        if (admin.getUsername()!=null) {
            foundUser.get().setUsername(admin.getUsername());
        }
        if (admin.getPassword() != null) {
            foundUser.get().setPassword(admin.getPassword());
        }
        adminRepository.save(foundUser.get());
        return convertToAdminDto(foundUser.get());
    }

    public void remove(Long id) {
        Optional<User> foundUser = Optional.of(userRepository.getById(id));
        if (foundUser.isPresent()) {
            userRepository.delete(foundUser.get());
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User doesn't exist");
        }
    }

    public AdminDTO convertToAdminDto(Admin user) {
        AdminDTO adminDTO = modelMapper.map(user, AdminDTO.class);
        return adminDTO;
    }

    public AccountHolderDTO convertToAccHolderDto(AccountHolder user) {
        AccountHolderDTO accountHolderDTO = modelMapper.map(user, AccountHolderDTO.class);
        return accountHolderDTO;
    }

    public ThirdPartyDTO convertToThirdPartyDto(ThirdParty user) {
        ThirdPartyDTO thirdPartyDTO = modelMapper.map(user, ThirdPartyDTO.class);
        return thirdPartyDTO;
    }
}

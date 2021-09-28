package com.ironhack.midterm.service.impl;

import com.ironhack.midterm.dao.users.User;
import com.ironhack.midterm.repository.users.UserRepository;
import com.ironhack.midterm.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> aUser = userRepository.findByUsername(s);

        if (!aUser.isPresent()) {
            throw new UsernameNotFoundException("User does not exist.");
        }

        CustomUserDetails customUserDetails = new CustomUserDetails(aUser.get());
        return customUserDetails;
    }


}

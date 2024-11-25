package com.kula.kula_project_backend.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kula.kula_project_backend.dao.UsersRepository;
import com.kula.kula_project_backend.entity.Users;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UsersRepository usersRepository;
    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    public CustomUserDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String emailOrPhoneNumber) throws UsernameNotFoundException {
        log.info("Loading user by email/phone number: {}", emailOrPhoneNumber);
        Users user = usersRepository.findByEmailOrPhoneNumber(emailOrPhoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email or phone number: " + emailOrPhoneNumber));

        log.info("Loaded user: {}", user.getUsername());
        return new User(emailOrPhoneNumber, user.getPasswordHash(), new ArrayList<>());
    }
}


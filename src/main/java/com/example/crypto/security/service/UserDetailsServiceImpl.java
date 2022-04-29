package com.example.crypto.security.service;

import com.example.crypto.security.model.CryptoUserDetails;
import com.example.crypto.security.model.User;
import com.example.crypto.security.repository.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    //@Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        User user = username.contains("@") ? userRepository.getUsersByEmailWithRoles(username) : userRepository.getUsersByUsernameWithRoles(username);

        if (user == null) throw new UsernameNotFoundException(username);
        //Hibernate.initialize(user.getRoles()); //need roles
        return new CryptoUserDetails(user);
    }
}
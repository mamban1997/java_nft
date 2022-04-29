package com.example.crypto.security.service;

import com.example.crypto.security.model.RegisterDto;
import com.example.crypto.security.model.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface IUserService {
    User registerNewUserAccount(RegisterDto registerDto);
    User getCurrentUser();
}

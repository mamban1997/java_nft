package com.example.crypto.security.service;

import com.example.crypto.security.model.CryptoUserDetails;
import com.example.crypto.security.model.RegisterDto;
import com.example.crypto.security.model.Role;
import com.example.crypto.security.model.User;
import com.example.crypto.security.repository.RoleRepository;
import com.example.crypto.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

@Service
@Transactional
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerNewUserAccount(RegisterDto registerDto) {
        User userByLogin = userRepository.getUsersByUsername(registerDto.getUsername());
        if (userByLogin!=null){
            return null;
        }
        User userByEmail = userRepository.getUsersByEmail(registerDto.getEmail());
        if (userByEmail!=null){
            return null;
        }

        HashSet<Role> userRoles = new HashSet<>();
        userRoles.add(roleRepository.findByName("ROLE_USER"));
        User user = User.builder()
                .username(registerDto.getUsername())
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .roles(userRoles)
                .regDate(LocalDateTime.now())
                .build();

        userRepository.save(user);
        return user;
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof String && principal.equals("anonymousUser")) {
            return null;
        } else if (principal instanceof CryptoUserDetails) {
            return ((CryptoUserDetails) principal).getUser();
        }
        Logger.getLogger("qwe").info("Неизвестная ситуация в гет куррент юзер");
        return null;
    }
}

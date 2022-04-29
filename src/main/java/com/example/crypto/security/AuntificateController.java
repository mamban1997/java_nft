package com.example.crypto.security;

import com.example.crypto.security.model.RegisterDto;
import com.example.crypto.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuntificateController {


    private final UserService userService;

    @Autowired
    public AuntificateController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute
    private void addUser(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/registration")
    public String registrationPage() {
        return "registration";
    }

    @PostMapping("/registration")
    public String registerNewUser(RegisterDto registerDto) {
        userService.registerNewUserAccount(registerDto);
        return "login";
    }
}

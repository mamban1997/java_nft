package com.example.crypto.security;

import com.example.crypto.entities.VerificationToken;
import com.example.crypto.services.MailService;
import com.example.crypto.security.model.RegisterDto;
import com.example.crypto.security.service.UserService;
import com.example.crypto.services.VerificationTokenService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuntificateController {


    private final UserService userService;
    private final MailService mailService;
    private final VerificationTokenService codesService;
    //private final VerificationTokenService verificationCodesService;

    @Autowired
    public AuntificateController(UserService userService, MailService mailService, VerificationTokenService codesService) {
        this.userService = userService;
        this.mailService = mailService;
        this.codesService = codesService;
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

    @PostMapping("/registration/send_code")
    public ResponseEntity<String> sendValidationEmailCode(@RequestBody ObjectNode json) {
        String email = json.get("email").asText();
        VerificationToken verificationToken = codesService.generateValidationCode(email, 3600);
        mailService.sendSimpleMail(mailService.NOTICE_EMAIL, email, "Secret code", "Ваш секретный код: " + verificationToken.getToken());
        return ResponseEntity.ok("email has sent");
    }
}

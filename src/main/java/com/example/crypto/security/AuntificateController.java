package com.example.crypto.security;

import com.example.crypto.entities.VerificationToken;
import com.example.crypto.security.model.RegisterDto;
import com.example.crypto.security.service.UserService;
import com.example.crypto.services.MailService;
import com.example.crypto.services.VerificationTokenService;
import com.sun.mail.smtp.SMTPAddressFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.mail.SendFailedException;
import javax.validation.Valid;

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
    public ResponseEntity<String> sendValidationEmailCode(@Valid @RequestBody RegisterDto registerDto, BindingResult bindingResult) {
        if (bindingResult.getFieldError("email") != null) {
            return new ResponseEntity<>("Email address " + registerDto.getEmail() + " isn't correct", HttpStatus.NOT_ACCEPTABLE);
        }
        String email = registerDto.getEmail();
        VerificationToken verificationToken = codesService.generateValidationCodeForEmail(email, 3600);
        if (verificationToken != null) {
            System.out.println(verificationToken.getToken());
            try {
                mailService.sendSimpleMail(mailService.NOTICE_EMAIL, email, "Secret code", "Ваш секретный код: " + verificationToken.getToken());
            } catch (MailSendException e) {
                System.out.println(e.getFailedMessages());
                codesService.disactivateValidationCodeForEmail(email);
                return new ResponseEntity<>("Email address " + registerDto.getEmail() + " isn't correct or e-mail server is unavailable", HttpStatus.NOT_ACCEPTABLE);
            }
            return ResponseEntity.ok("Email has been sent!");
        } else {
            return new ResponseEntity<>("Email hasn't been sent! Please, try again after 1 minute", HttpStatus.NOT_ACCEPTABLE);
        }
    }
}

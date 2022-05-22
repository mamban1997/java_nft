package com.example.crypto.services;

import com.example.crypto.entities.VerificationToken;
import com.example.crypto.repositories.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class VerificationTokenService {
    VerificationTokenRepository codesRepository;

    @Autowired
    public VerificationTokenService(VerificationTokenRepository codesRepository) {
        this.codesRepository = codesRepository;
    }

    public VerificationToken generateValidationCode(String email, Integer secondToExpiry){
        String code = generateCode();
        VerificationToken token = VerificationToken.builder().expiryDate(LocalDateTime.now().plusSeconds(secondToExpiry)).token(code).active(true).action(1).email(email).build();
        codesRepository.save(token);
        return token;
    }

    private String generateCode(){
        List<String> chars = new ArrayList<>();
        Random random = new Random();
        while (chars.size()<6){
            Integer randomValue = random.nextInt(10);
            chars.add(String.valueOf(randomValue));
        }
        chars.add(3,"-");

        return String.join("", chars);
    }

}

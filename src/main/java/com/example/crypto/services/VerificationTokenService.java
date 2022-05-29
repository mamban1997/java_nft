package com.example.crypto.services;

import com.example.crypto.entities.VerificationToken;
import com.example.crypto.repositories.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.crypto.entities.VerificationToken.Action;

@Service
public class VerificationTokenService {
    VerificationTokenRepository tokenRepository;

    private final Integer secondForRetryCreateToken = 30;

    @Autowired
    public VerificationTokenService(VerificationTokenRepository codesRepository) {
        this.tokenRepository = codesRepository;
    }

    @Transactional
    public VerificationToken generateValidationCodeForEmail(String email, Integer secondToExpiry) {
        VerificationToken oldToken = tokenRepository.findByEmailAndActionEqualsAndActive(email, Action.ACTION_REGISTRATION, true);
        LocalDateTime now = LocalDateTime.now();
        if (oldToken != null) {
            if (oldToken.getCreateDate().plusSeconds(secondForRetryCreateToken).isBefore(now)) {
                oldToken.setActive(false);
                tokenRepository.save(oldToken);
            } else {
                return null;
            }
        }
        String code = generateCode();
        VerificationToken token = VerificationToken.builder()
                .createDate(now)
                .expiryDate(now.plusSeconds(secondToExpiry))
                .token(code)
                .active(true)
                .action(Action.ACTION_REGISTRATION)
                .email(email)
                .build();
        tokenRepository.save(token);
        return token;
    }

    public void disactivateValidationCodeForEmail(String email) {
        VerificationToken oldToken = tokenRepository.findByEmailAndActionEqualsAndActive(email, Action.ACTION_REGISTRATION, true);
        if (oldToken != null) {
            oldToken.setActive(false);
            tokenRepository.save(oldToken);
        }
    }

    private String generateCode() {
        List<String> chars = new ArrayList<>();
        Random random = new Random();
        while (chars.size() < 6) {
            Integer randomValue = random.nextInt(10);
            chars.add(String.valueOf(randomValue));
        }
        chars.add(3, "-");

        return String.join("", chars);
    }

}

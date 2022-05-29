package com.example.crypto.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "verification_tokens")
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;
    private String email;
    private boolean active;
    private LocalDateTime createDate;
    private LocalDateTime expiryDate;
    private Action action;
    //1-reg
    //2-load nft

public enum Action{
    ACTION_REGISTRATION,
    ACTION_LOAD_NFT,
    ACTION_TEST
}

}

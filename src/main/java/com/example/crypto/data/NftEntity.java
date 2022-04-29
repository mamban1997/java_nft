package com.example.crypto.data;

import com.example.crypto.security.model.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class NftEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    private String nftName;
    @Column(unique = true)
    private String uniqNumber;//GUID like e02fd0e4-00fd-090A-ca30-0d00a0038ba0
    private String alias;
    private String picture;
    private LocalDateTime createDate;

    @ManyToOne
    @ToString.Exclude
    private User currentOwner;
}

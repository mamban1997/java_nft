package com.example.crypto.security.model;

import com.example.crypto.data.NftEntity;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private Integer age;
    private LocalDateTime regDate;

    @ManyToMany(cascade=CascadeType.ALL)
    @ToString.Exclude
    private Set<Role> roles;

    @OneToMany(mappedBy = "currentOwner")
    @ToString.Exclude
    private List<NftEntity> nft;
}

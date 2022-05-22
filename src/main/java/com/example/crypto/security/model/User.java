package com.example.crypto.security.model;

import com.example.crypto.entities.NftEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

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
    private Double balance;

    @ManyToMany
    @ToString.Exclude
    private Set<Role> roles;

    @OneToMany(mappedBy = "currentOwner")
    @ToString.Exclude
    private List<NftEntity> nft;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) && username.equals(user.username) && email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email);
    }
    public String getStringBalance() {
        return String.format("%,.2f", getBalance());
    }
}

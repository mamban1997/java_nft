package com.example.crypto.security.repository;


import com.example.crypto.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {


    @Query("FROM User user JOIN FETCH user.roles WHERE user.username=:login")
    User getUsersByUsernameWithRoles(String login);
    User getUsersByUsername(String login);


    @Query("FROM User user JOIN FETCH user.roles WHERE user.email=:email")
    User getUsersByEmailWithRoles(String email);
    User getUsersByEmail(String email);




}
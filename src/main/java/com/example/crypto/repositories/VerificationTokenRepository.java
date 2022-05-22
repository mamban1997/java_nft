package com.example.crypto.repositories;

import com.example.crypto.entities.NftEntity;
import com.example.crypto.entities.VerificationToken;
import com.example.crypto.security.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
}

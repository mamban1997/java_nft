package com.example.crypto.data;

import com.example.crypto.security.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NftEntityRepository extends JpaRepository<NftEntity, Long> {
    NftEntity findByUniqNumber(String uniqNumber);
    NftEntity findByAlias(String alias);

    Page<NftEntity> findAllByHidden(Pageable pageable, Boolean hidden);

    Page<NftEntity> findAllByCurrentOwner(User currentOwner, Pageable pageable);
}

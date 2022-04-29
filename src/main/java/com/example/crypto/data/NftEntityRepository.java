package com.example.crypto.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NftEntityRepository extends JpaRepository<NftEntity, Long> {
    NftEntity findByUniqNumber(String uniqNumber);
    NftEntity findByAlias(String alias);
}

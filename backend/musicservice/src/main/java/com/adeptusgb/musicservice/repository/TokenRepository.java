package com.adeptusgb.musicservice.repository;

import com.adeptusgb.musicservice.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query(value = "SELECT * FROM token LIMIT 1", nativeQuery = true)
    Token findFirst();
}

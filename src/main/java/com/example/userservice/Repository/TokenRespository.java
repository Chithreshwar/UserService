package com.example.userservice.Repository;

import com.example.userservice.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRespository extends JpaRepository<Token, Integer> {
    Optional<Token> findTokenByValue(String value);
}

package com.example.userservice.dto;

import com.example.userservice.entities.Token;
import lombok.Data;

@Data
public class ValidateTokenDto {
    private String token;
}

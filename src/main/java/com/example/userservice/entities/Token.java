package com.example.userservice.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String value;
    @ManyToOne
    private User user;
    private Date expiresAt;
    private boolean isActive;
}

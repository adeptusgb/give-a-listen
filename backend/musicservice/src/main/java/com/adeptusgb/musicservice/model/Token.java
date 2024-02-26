package com.adeptusgb.musicservice.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String accessToken;
    private String type;
    private LocalDateTime expiresAt;

    public Token(String accessToken, String type, long expiresIn) {
        this.accessToken = accessToken;
        this.type = type;
        expiresAt = LocalDateTime.now().plusSeconds(expiresIn);
    }
}

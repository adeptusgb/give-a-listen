package com.adeptusgb.musicservice.model.spotify;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "spotify_token")
public class SpotifyToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String accessToken;
    private String type;
    private LocalDateTime expiresAt;

    public SpotifyToken(String accessToken, String type, long expiresIn) {
        this.accessToken = accessToken;
        this.type = type + " "; // without a space, requests would have to add it manually
        expiresAt = LocalDateTime.now().plusSeconds(expiresIn);
    }
}

package com.adeptusgb.musicservice.repository;

import com.adeptusgb.musicservice.model.spotify.SpotifyToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SpotifyTokenRepository extends JpaRepository<SpotifyToken, Long> {
    @Query(value = "SELECT * FROM spotify_token LIMIT 1", nativeQuery = true)
    SpotifyToken findFirst();
}

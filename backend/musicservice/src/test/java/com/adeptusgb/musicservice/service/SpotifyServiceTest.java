package com.adeptusgb.musicservice.service;

import com.adeptusgb.musicservice.model.SpotifySearchResponse;
import com.adeptusgb.musicservice.repository.TokenRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class SpotifyServiceTest {

    @Mock private TokenRepository tokenRepository;
    @InjectMocks private SpotifyService spotifyService;

    @Test
    void searchAlbums() {
        // given
        String query = "Deathconsciousness";

        // when
        SpotifySearchResponse res = spotifyService.spotifySearch(query, "album");

        // then
        verify(tokenRepository, times(1)).findFirst();
        assertThat(res.getStatusCode()).isEqualTo(200);
    }

    @Test
    void searchArtists() {
        // given
        String query = "Godspeed You! Black Emperor";

        // when
        SpotifySearchResponse res = spotifyService.spotifySearch(query, "artist");

        // then
        verify(tokenRepository, times(1)).findFirst();
        assertThat(res.getStatusCode()).isEqualTo(200);
    }

    @Test
    void searchTracks() {
        // given
        String query = "Weird Fishes/Arpeggi";

        // when
        SpotifySearchResponse res = spotifyService.spotifySearch(query, "track");

        // then
        verify(tokenRepository, times(1)).findFirst();
        assertThat(res.getStatusCode()).isEqualTo(200);
    }
}
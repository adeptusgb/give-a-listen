package com.adeptusgb.musicservice.service;

import com.adeptusgb.musicservice.repository.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
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
    @Disabled
    void getAccessTokenShouldReturnToken() {
        // when
        String token = null;
        try {
            token = spotifyService.getAccessToken();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        // then
        assertNotNull(token);
    }

    @Test
    void searchAlbums() {
        // given
        String query = "Deathconsciousness";

        String albums = spotifyService.spotifySearch(query, "album");
        verify(tokenRepository, times(1)).findFirst();
        assertThat(albums).isNotNull();
    }

    @Test
    void searchArtists() {
        // given
        String query = "Godspeed You! Black Emperor";

        String artists = spotifyService.spotifySearch(query, "artist");
        verify(tokenRepository, times(1)).findFirst();
        assertThat(artists).isNotNull();
    }

    @Test
    void searchTracks() {
        // given
        String query = "Weird Fishes/Arpeggi";

        String tracks = spotifyService.spotifySearch(query, "track");
        verify(tokenRepository, times(1)).findFirst();
        assertThat(tracks).isNotNull();
    }
}
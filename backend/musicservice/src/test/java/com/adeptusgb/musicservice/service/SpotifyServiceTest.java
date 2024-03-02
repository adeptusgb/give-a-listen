package com.adeptusgb.musicservice.service;

import com.adeptusgb.musicservice.model.Artist;
import com.adeptusgb.musicservice.model.spotify.SpotifyRecommendedResponse;
import com.adeptusgb.musicservice.model.spotify.SpotifySearchResponse;
import com.adeptusgb.musicservice.model.Track;
import com.adeptusgb.musicservice.repository.SpotifyTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class SpotifyServiceTest {

    @Mock private SpotifyTokenRepository tokenRepository;
    @InjectMocks private SpotifyService spotifyService;

    @Test
    void searchAlbums() {
        // given
        String query = "Deathconsciousness";

        // when
        SpotifySearchResponse res = spotifyService.spotifySearch(query, "album", 1);

        // then
        verify(tokenRepository, times(1)).findFirst();
        assertThat(res.getStatusCode()).isEqualTo(200);
    }

    @Test
    void searchArtists() {
        // given
        String query = "Godspeed You! Black Emperor";

        // when
        SpotifySearchResponse res = spotifyService.spotifySearch(query, "artist", 1);

        // then
        verify(tokenRepository, times(1)).findFirst();
        assertThat(res.getStatusCode()).isEqualTo(200);
    }

    @Test
    void searchTracks() {
        // given
        String query = "Weird Fishes/Arpeggi";

        // when
        SpotifySearchResponse res = spotifyService.spotifySearch(query, "track", 1);

        // then
        verify(tokenRepository, times(1)).findFirst();
        assertThat(res.getStatusCode()).isEqualTo(200);
    }

    // add a test for getRecommendedTracks here
    @Test
    void getRecommendedTracks() {
        // given
        String trackQuery = "Wish You Were Here";
        String artistQuery = "Pink Floyd";

        List<String> tracks = spotifyService.spotifySearch(trackQuery, "track",1)
                .getTracks().stream().map(Track::getSpotifyId).toList();
        List<String> artists = spotifyService.spotifySearch(artistQuery, "artist",1)
                .getArtists().stream().map(Artist::getSpotifyId).toList();

        // when
        SpotifyRecommendedResponse res = spotifyService.getRecommendedTracks(tracks, artists, 1);

        // then
        System.out.println(res.getTracks());
        verify(tokenRepository, times(3)).findFirst();
        assertThat(res.getStatusCode()).isEqualTo(200);
        assertThat(res.getTracks()).isNotEmpty();
    }
}
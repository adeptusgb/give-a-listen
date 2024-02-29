package com.adeptusgb.musicservice.service;

import com.adeptusgb.musicservice.DTO.getRecommendedRequestDTO;
import com.adeptusgb.musicservice.model.Album;
import com.adeptusgb.musicservice.model.Artist;
import com.adeptusgb.musicservice.model.Track;
import com.adeptusgb.musicservice.model.spotify.SpotifySearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MusicService {

    private final SpotifyService spotifyService;

    private SpotifySearchResponse searchForAlbumOrArtistOrTrack(String query, String type) {
        return spotifyService.spotifySearch(query, type);
    }

    public List<Album> searchForAlbum(String query) {
        return searchForAlbumOrArtistOrTrack(query, "album").getAlbums();
    }

    public List<Artist> searchForArtist(String query) {
        return searchForAlbumOrArtistOrTrack(query, "artist").getArtists();
    }

    public List<Track> searchForTrack(String query) {
        return searchForAlbumOrArtistOrTrack(query, "track").getTracks();
    }

    public List<Track> getRecommendedTracks(getRecommendedRequestDTO seeds) {

        if (seeds.tracks().size() + seeds.artists().size() > 5) {
            // eventually will be handled by a global exception handler, with a custom error message
            throw new IllegalArgumentException(
                    "Exceeded the maximum number of seeds.\nmax=5\ngot=" + (seeds.tracks().size() + seeds.artists().size())
            );
        }

        return spotifyService.getRecommendedTracks(seeds.tracks(), seeds.artists()).getTracks();
    }
}

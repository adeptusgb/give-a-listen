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

    private SpotifySearchResponse searchForAlbumOrArtistOrTrack(String query, String type, Integer limit) {
        if (limit > 50) limit = 50; // max limit
        else if (limit < 1) limit = 1; // min limit

        return spotifyService.spotifySearch(query, type, limit);
    }

    public List<Album> searchForAlbum(String query, Integer limit) {
        return searchForAlbumOrArtistOrTrack(query, "album", limit).getAlbums();
    }

    public List<Artist> searchForArtist(String query, Integer limit) {
        return searchForAlbumOrArtistOrTrack(query, "artist", limit).getArtists();
    }

    public List<Track> searchForTrack(String query, Integer limit) {
        return searchForAlbumOrArtistOrTrack(query, "track", limit).getTracks();
    }

    public List<Track> getRecommendedTracks(getRecommendedRequestDTO seeds, Integer limit) {

        if (seeds.tracks().size() + seeds.artists().size() > 5) {
            // eventually will be handled by a global exception handler, with a custom error message
            throw new IllegalArgumentException(
                    "Exceeded the maximum number of seeds.\nmax=5\ngot=" + (seeds.tracks().size() + seeds.artists().size())
            );
        }

        if (limit > 50) limit = 50; // max limit
        else if (limit < 1) limit = 1; // min limit

        return spotifyService.getRecommendedTracks(seeds.tracks(), seeds.artists(), limit).getTracks();
    }
}

package com.adeptusgb.musicservice.model.spotify;

import com.adeptusgb.musicservice.model.Album;
import com.adeptusgb.musicservice.model.Artist;
import com.adeptusgb.musicservice.model.Track;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpotifySearchResponse {
    private List<Track> tracks;
    private List<Artist> artists;
    private List<Album> albums;
    private int statusCode;
}


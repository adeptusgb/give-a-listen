package com.adeptusgb.musicservice.model.spotify;

import com.adeptusgb.musicservice.model.Album;
import com.adeptusgb.musicservice.model.Artist;
import com.adeptusgb.musicservice.model.Track;
import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpotifySearchResponse {
    private ArrayList<Track> tracks;
    private ArrayList<Artist> artists;
    private ArrayList<Album> albums;
    private int statusCode;
}


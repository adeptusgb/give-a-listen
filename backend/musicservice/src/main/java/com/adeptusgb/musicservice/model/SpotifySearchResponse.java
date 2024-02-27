package com.adeptusgb.musicservice.model;

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


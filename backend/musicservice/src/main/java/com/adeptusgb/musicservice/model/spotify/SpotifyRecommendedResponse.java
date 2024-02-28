package com.adeptusgb.musicservice.model.spotify;

import com.adeptusgb.musicservice.model.Track;
import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpotifyRecommendedResponse {
    private ArrayList<Track> tracks;
    private int statusCode;
}

package com.adeptusgb.musicservice.model.spotify;

import com.adeptusgb.musicservice.model.Track;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpotifyRecommendedResponse {
    private List<Track> tracks;
    private int statusCode;
}

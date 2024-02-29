package com.adeptusgb.musicservice.DTO;

import java.util.List;

public record getRecommendedRequestDTO(
        List<String> tracks, // tracks Spotify IDs
        List<String> artists // artists Spotify IDs
) {
}

package com.adeptusgb.musicservice.controller;

import com.adeptusgb.musicservice.model.spotify.SpotifySearchResponse;
import com.adeptusgb.musicservice.service.SpotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "api/v1/music")
public class MusicController {

    private final SpotifyService spotifyService;

    @GetMapping(path = "search")
    public ResponseEntity<SpotifySearchResponse> searchMusic(
            @RequestParam String query,
            @RequestParam String type) {
        return new ResponseEntity<>(spotifyService.spotifySearch(query, type), HttpStatus.OK);
    }

}

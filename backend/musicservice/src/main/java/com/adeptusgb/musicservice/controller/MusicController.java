package com.adeptusgb.musicservice.controller;

import com.adeptusgb.musicservice.service.SpotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "api/v1/music")
public class MusicController {

    private final SpotifyService spotifyService;

    @GetMapping(path = "search")
    public String searchMusic(
            @RequestParam String query,
            @RequestParam String type) {
        return spotifyService.spotifySearch(query, type);
    }

}

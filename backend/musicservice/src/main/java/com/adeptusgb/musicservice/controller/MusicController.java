package com.adeptusgb.musicservice.controller;

import com.adeptusgb.musicservice.DTO.getRecommendedRequestDTO;
import com.adeptusgb.musicservice.model.Album;
import com.adeptusgb.musicservice.model.Artist;
import com.adeptusgb.musicservice.model.Track;
import com.adeptusgb.musicservice.service.MusicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "api/v1")
public class MusicController {

    private final MusicService musicService;

    @GetMapping(path = "search/album")
    public ResponseEntity<List<Album>> searchAlbum(
            @RequestParam String q,
            @RequestParam (required = false, defaultValue = "20") Integer limit
    ) {
        return new ResponseEntity<>(musicService.searchForAlbum(q, limit), HttpStatus.OK);
    }

    @GetMapping(path = "search/artist")
    public ResponseEntity<List<Artist>> searchArtist(
            @RequestParam String q,
            @RequestParam (required = false, defaultValue = "20") Integer limit
    ) {
        return new ResponseEntity<>(musicService.searchForArtist(q, limit), HttpStatus.OK);
    }

    @GetMapping(path = "search/track")
    public ResponseEntity<List<Track>> searchTrack(
            @RequestParam String q,
            @RequestParam (required = false, defaultValue = "20") Integer limit
    ) {
        return new ResponseEntity<>(musicService.searchForTrack(q, limit), HttpStatus.OK);
    }

    @PostMapping(path = "recommendation")
    public ResponseEntity<List<Track>> getRecommendation(
            @RequestBody getRecommendedRequestDTO seeds,
            @RequestParam (required = false, defaultValue = "20") Integer limit
    ) {
        return new ResponseEntity<>(musicService.getRecommendedTracks(seeds, limit), HttpStatus.OK);
    }

}

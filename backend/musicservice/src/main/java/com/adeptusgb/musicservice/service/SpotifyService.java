package com.adeptusgb.musicservice.service;

import com.adeptusgb.musicservice.model.*;
import com.adeptusgb.musicservice.repository.TokenRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class SpotifyService {

    private final TokenRepository tokenRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Dotenv dotenv = Dotenv.configure()
            .directory("./src/main/resources")
            .load();
    private final String SPOTIFY_CREDENTIAL = dotenv.get("SPOTIFY_CREDENTIAL");
    private final String SPOTIFY_SECRET = dotenv.get("SPOTIFY_SECRET");
    private static final String SPOTIFY_TOKEN_URL = "https://accounts.spotify.com/api/token";
    private static final String SPOTIFY_API_URL = "https://api.spotify.com/v1";

    public String getAccessToken() throws URISyntaxException {
        Token token = tokenRepository.findFirst();

        // early return if token is still valid
        if (token != null && token.getExpiresAt().isAfter(LocalDateTime.now())) {
            return token.getAccessToken();
        }

        // if the token expired or doesn't exist, get a new one
        tokenRepository.deleteAll();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(SPOTIFY_TOKEN_URL))
                .headers("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("grant_type=client_credentials&client_id="+ SPOTIFY_CREDENTIAL +"&client_secret="+ SPOTIFY_SECRET))
                .build();

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode jsonNode = objectMapper.readTree(response.body());

            Token newToken = new Token(
                    jsonNode.get("access_token").asText(),
                    jsonNode.get("token_type").asText(),
                    jsonNode.get("expires_in").asLong()
            );

            tokenRepository.save(newToken);
            return newToken.getAccessToken();
        } catch (Exception e) {
            System.out.println(e.getMessage()); // need to handle exception properly to prevent null pointer exception
            return null;
        }
    }

    // need to save tracks, artists, and albums to the database
    public SpotifySearchResponse spotifySearch(String query, String type) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(SPOTIFY_API_URL + "/search?q=" + URLEncoder.encode(query) + "&type=" + type))
                    .headers("Authorization", "Bearer " + getAccessToken())
                    .GET()
                    .build();

            try (HttpClient client = HttpClient.newHttpClient()) {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JsonNode jsonNode = objectMapper.readTree(response.body());

                ArrayList<Track> tracksList = new ArrayList<>();
                ArrayList<Artist> artistsList = new ArrayList<>();
                ArrayList<Album> albumsList = new ArrayList<>();

                JsonNode tracksNode = jsonNode.get("tracks");
                JsonNode artistsNode = jsonNode.get("artists");
                JsonNode albumsNode = jsonNode.get("albums");

                if (tracksNode != null) {
                    for (final JsonNode track : tracksNode.get("items")) {
                        tracksList.add(
                                new Track(
                                        track.get("name").asText(),
                                        track.get("id").asText() // spotify ID, not the database ID!!!
                                )
                        );
                    }
                }

                if (artistsNode != null) {
                    for (final JsonNode artist : artistsNode.get("items")) {
                        artistsList.add(
                                new Artist(
                                        artist.get("name").asText(),
                                        artist.get("id").asText() // spotify ID, not the database ID!!!
                                )
                        );
                    }
                }

                if (albumsNode != null) {
                    for (final JsonNode album : albumsNode.get("items")) {
                        albumsList.add(
                                new Album(
                                        album.get("name").asText(),
                                        album.get("id").asText() // spotify ID, not the database ID!!!
                                )
                        );
                    }
                }

                return SpotifySearchResponse.builder()
                        .tracks(tracksList)
                        .artists(artistsList)
                        .albums(albumsList)
                        .statusCode(response.statusCode())
                        .build();
            } catch (Exception e) {
                System.out.println(e.getMessage()); // need to handle exception properly to prevent null pointer exception
                return null;
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}

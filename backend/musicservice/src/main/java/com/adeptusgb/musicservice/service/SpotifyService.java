package com.adeptusgb.musicservice.service;

import com.adeptusgb.musicservice.model.*;
import com.adeptusgb.musicservice.model.spotify.SpotifyRecommendedResponse;
import com.adeptusgb.musicservice.model.spotify.SpotifySearchResponse;
import com.adeptusgb.musicservice.model.spotify.SpotifyToken;
import com.adeptusgb.musicservice.repository.SpotifyTokenRepository;
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

    private final SpotifyTokenRepository tokenRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Dotenv dotenv = Dotenv.configure()
            .directory("./src/main/resources")
            .load();
    private final String SPOTIFY_CREDENTIAL = dotenv.get("SPOTIFY_CREDENTIAL");
    private final String SPOTIFY_SECRET = dotenv.get("SPOTIFY_SECRET");
    private static final String SPOTIFY_TOKEN_URL = "https://accounts.spotify.com/api/token";
    private static final String SPOTIFY_API_URL = "https://api.spotify.com/v1";

    public SpotifyToken getAccessToken() throws URISyntaxException {
        SpotifyToken token = tokenRepository.findFirst();

        // early return if token is still valid
        if (token != null && token.getExpiresAt().isAfter(LocalDateTime.now())) {
            return token;
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

            SpotifyToken newToken = new SpotifyToken(
                    jsonNode.get("access_token").asText(),
                    jsonNode.get("token_type").asText(),
                    jsonNode.get("expires_in").asLong()
            );

            tokenRepository.save(newToken);
            return newToken;
        } catch (Exception e) {
            System.out.println(e.getMessage()); // need to handle exception properly to prevent null pointer exception
            return null;
        }
    }

    // need to save tracks, artists, and albums to the database //
    public SpotifySearchResponse spotifySearch(String query, String type, int limit) {
        try {
            SpotifyToken token = getAccessToken();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(SPOTIFY_API_URL + "/search?q="
                            + URLEncoder.encode(query)
                            + "&type=" + type
                            + "&limit=" + limit)
                    )
                    .headers("Authorization", token.getType() + token.getAccessToken())
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

    // default limit is 20 //
    public SpotifySearchResponse spotifySearch(String query, String type) {
        return spotifySearch(query, type, 20);
    }

    // max of 5 seeds total!!! //
    public SpotifyRecommendedResponse getRecommendedTracks(ArrayList<Track> tracks, ArrayList<Artist> artists) {
        ArrayList<String> tracksId = new ArrayList<>();
        ArrayList<String> artistsId = new ArrayList<>();

        // eventually will check this in the music service
        for (Track track : tracks) {
            if (tracksId.size() != 5) {
                tracksId.add(track.getSpotifyId());
            }
        }

        for (Artist artist : artists) {
            if (artistsId.size() != 5) {
                artistsId.add(artist.getSpotifyId());
            }
        }

        if (tracksId.size() + artistsId.size() > 5) {
            throw new IllegalArgumentException(
                    "Exceeded the maximum number of seeds.\nmax=5\ngot=" + (tracksId.size() + artistsId.size())
            );
        }

        try {
            SpotifyToken token = getAccessToken();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(SPOTIFY_API_URL
                            + "/recommendations?"
                            + "seed_tracks=" + URLEncoder.encode(String.join(",", tracksId))
                            + "&seed_artists=" + URLEncoder.encode(String.join(",", artistsId)))
                    )
                    .headers("Authorization", token.getType() + token.getAccessToken())
                    .GET()
                    .build();

            try (HttpClient client = HttpClient.newHttpClient()) {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JsonNode jsonNode = objectMapper.readTree(response.body());

                ArrayList<Track> tracksList = new ArrayList<>();

                for (final JsonNode track : jsonNode.get("tracks")) {
                    tracksList.add(
                            new Track(
                                    track.get("name").asText(),
                                    track.get("id").asText() // spotify ID, not the database ID!!!
                            )
                    );
                }

                return SpotifyRecommendedResponse.builder()
                        .tracks(tracksList)
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

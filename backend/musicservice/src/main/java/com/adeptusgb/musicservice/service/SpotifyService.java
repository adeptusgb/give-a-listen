package com.adeptusgb.musicservice.service;

import com.adeptusgb.musicservice.model.Token;
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

    // eventually this method will get the access token from the database and check if it's still valid
    // for now, it just gets a new access token every time it's called
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

    public String spotifySearch(String query, String type) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(SPOTIFY_API_URL + "/search?q=" + URLEncoder.encode(query) + "&type=" + type))
                    .headers("Authorization", "Bearer " + getAccessToken())
                    .GET()
                    .build();

            try (HttpClient client = HttpClient.newHttpClient()) {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                System.out.println(response.body()); // take sout out of production code later
                return response.body();
            } catch (Exception e) {
                System.out.println(e.getMessage()); // need to handle exception properly to prevent null pointer exception
                return null;
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}

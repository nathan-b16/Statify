package com.example.LoginSystem.Service;

import com.example.LoginSystem.DTO.RecommendationDTO;
import com.example.LoginSystem.DTO.RecommendationWrapper;
import com.example.LoginSystem.Model.Track.TrackEntity;
import com.example.LoginSystem.Repo.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class RecommendationService {
    @Autowired
    TrackRepository trackRepository;

    @Autowired
    SpotifyApi spotifyApi;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.reccobeats.com/v1")
            .build();

    @Cacheable(value = "recommendations")
    public List<RecommendationDTO> TrackReccomendation() {
        List<TrackEntity> tracks = trackRepository.findAll();
        List<RecommendationDTO> recommendations = new ArrayList<>();
        refreshSpotifyToken();

        if (tracks.isEmpty()) {
            return recommendations;
        }
        TrackEntity seed = tracks.getFirst();
        try {
            RecommendationWrapper response = webClient.get()
                    .uri("/track/recommendation?size=20&seeds=" + seed.getSpotifyId())
                    .retrieve()
                    .bodyToMono(RecommendationWrapper.class)
                    .block();

            if (response != null && response.getContent() != null) {
                for (RecommendationDTO dto : response.getContent()) {
                    fetchSpotifyData(dto.getIsrc(), dto);
                    recommendations.add(dto);
                }
            }
        } catch (WebClientResponseException e) {
            System.err.println("API error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        }

        return recommendations;
    }

    private void fetchSpotifyData(String isrc, RecommendationDTO dto) {
        try {
            Track[] tracks = spotifyApi.searchTracks("isrc:" + isrc)
                    .build()
                    .execute()
                    .getItems();

            if (tracks != null && tracks.length > 0) {
                dto.setId(tracks[0].getId());

                if (tracks[0].getAlbum().getImages().length > 0) {
                    dto.setImageUrl(tracks[0].getAlbum().getImages()[0].getUrl());
                }
            }
        } catch (Exception e) {
            System.err.println("fetch failed: " + e.getMessage());
        }
    }

    private void refreshSpotifyToken() {
        try {
            ClientCredentialsRequest request = spotifyApi.clientCredentials().build();
            ClientCredentials credentials = request.execute();
            spotifyApi.setAccessToken(credentials.getAccessToken());
        } catch (Exception e) {
            System.err.println("Token refresh failed: " + e.getMessage());
        }
    }
}
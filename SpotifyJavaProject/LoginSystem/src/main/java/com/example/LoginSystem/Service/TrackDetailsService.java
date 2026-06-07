package com.example.LoginSystem.Service;


import com.example.LoginSystem.DTO.AudioFeaturesDTO;
import com.example.LoginSystem.DTO.AudioFeaturesResponseDTO;
import com.example.LoginSystem.Model.Track.TrackDetailsEntity;
import com.example.LoginSystem.Model.Track.TrackEntity;
import com.example.LoginSystem.Repo.TrackDetailsRepository;
import com.example.LoginSystem.Repo.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TrackDetailsService {

    @Autowired
    private TrackDetailsRepository trackDetailsRepository;

    @Autowired
    private TrackRepository trackRepository;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.reccobeats.com/v1")
            .build();

    public void getTrackIds()
    {
        List<TrackEntity> tracks = trackRepository.findAll();
        if (tracks.isEmpty()) return;

        String trackIds = tracks.stream()
                .map(TrackEntity::getSpotifyId)
                .collect(Collectors.joining(","));

        try {
            AudioFeaturesResponseDTO response = webClient.get()
                    .uri("/audio-features?ids=" + trackIds)
                    .retrieve()
                    .bodyToMono(AudioFeaturesResponseDTO.class)
                    .block();

            if (response == null || response.getContent() == null) return;

            List<AudioFeaturesDTO> featuresList = response.getContent();
            List<TrackDetailsEntity> detailsList = new ArrayList<>();

            for (int i = 0; i < featuresList.size() && i < tracks.size(); i++) {
                AudioFeaturesDTO features = featuresList.get(i);
                if (features == null) continue;
                detailsList.add(mapToDetails(tracks.get(i).getSpotifyId(), features));
            }
            trackDetailsRepository.deleteAll();
            trackDetailsRepository.saveAll(detailsList);
        } catch (Exception e) {
            System.out.println("Error fetching features: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private TrackDetailsEntity mapToDetails(String spotifyId, AudioFeaturesDTO features) {
        TrackDetailsEntity details = new TrackDetailsEntity();
        details.setSpotifyId(spotifyId);
        details.setDanceability(features.getDanceability());
        details.setEnergy(features.getEnergy());
        details.setBpm(features.getTempo());
        details.setValence(features.getValence());
        details.setAcousticness(features.getAcousticness());
        details.setInstrumentalness(features.getInstrumentalness());
        details.setLiveness(features.getLiveness());
        details.setLoudness(features.getLoudness());
        details.setSpeechiness(features.getSpeechiness());
        details.setKey(features.getKey());
        details.setMode(features.getMode());
        return details;
    }

}

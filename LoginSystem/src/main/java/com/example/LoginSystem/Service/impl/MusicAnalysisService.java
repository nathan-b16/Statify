package com.example.LoginSystem.Service.impl;

import com.example.LoginSystem.Model.Track.TrackDetailsEntity;
import com.example.LoginSystem.Repo.TrackDetailsRepository;
import com.example.LoginSystem.Repo.TrackRepository;
import com.example.LoginSystem.Service.impl.TopItemServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MusicAnalysisService {

    private final TrackRepository trackRepo;
    private final TrackDetailsRepository detailsRepo;
    private final TopItemServiceImpl topItemService;
    private final WebClient geminiClient;
    private final String geminiApiKey;

    public MusicAnalysisService(
            TrackRepository trackRepo,
            TrackDetailsRepository detailsRepo,
            TopItemServiceImpl topItemService,
            @Value("${gemini.api.key:}") String geminiApiKey) {
        this.trackRepo = trackRepo;
        this.detailsRepo = detailsRepo;
        this.topItemService = topItemService;
        this.geminiApiKey = geminiApiKey;
        this.geminiClient = WebClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com/v1beta/models")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    /** Returns the same live top-5 tracks the Dashboard shows. */
    public List<Track> getTracks(String token) {
        return topItemService.getTopTracks(token);
    }

    public Map<String, Object> getAudioAverages() {
        List<TrackDetailsEntity> d = detailsRepo.findAll();
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("Energy",           avg(d.stream().mapToDouble(x -> x.getEnergy()           != null ? x.getEnergy()           : 0)));
        stats.put("Danceability",     avg(d.stream().mapToDouble(x -> x.getDanceability()      != null ? x.getDanceability()      : 0)));
        stats.put("Valence",          avg(d.stream().mapToDouble(x -> x.getValence()           != null ? x.getValence()           : 0)));
        stats.put("Acousticness",     avg(d.stream().mapToDouble(x -> x.getAcousticness()      != null ? x.getAcousticness()      : 0)));
        stats.put("Instrumentalness", avg(d.stream().mapToDouble(x -> x.getInstrumentalness()  != null ? x.getInstrumentalness()  : 0)));
        stats.put("Liveness",         avg(d.stream().mapToDouble(x -> x.getLiveness()          != null ? x.getLiveness()          : 0)));
        stats.put("Speechiness",      avg(d.stream().mapToDouble(x -> x.getSpeechiness()       != null ? x.getSpeechiness()       : 0)));
        stats.put("Avg BPM",          (int) Math.round(d.stream().mapToDouble(x -> x.getBpm() != null ? x.getBpm() : 0).average().orElse(0)));
        return stats;
    }

    /** Returns a human-written insight from Gemini, or null if unavailable. */
    public String generateInsight(String token) {
        List<Track> tracks = getTracks(token);
        if (tracks.isEmpty()) return null;

        String trackList = tracks.stream()
                .map(t -> t.getName() + " by " + t.getArtists()[0].getName())
                .collect(Collectors.joining(", "));

        Map<String, Object> avgs = getAudioAverages();

        String prompt = String.format(
                "You are a thoughtful friend who knows music well. " +
                "Write two short paragraphs of natural, flowing prose — no bullet points, no headers. " +
                "Paragraph 1 (2–3 sentences): describe who this person is through their music — mood, vibe, what drives them. " +
                "Paragraph 2 (2 sentences): suggest a genre, artist, or era they'd probably love and why. " +
                "Under 120 words total. Never mention audio feature names like energy or valence. " +
                "Top tracks: %s. " +
                "Context for tone only (don't quote numbers): energy %.2f, danceability %.2f, positivity %.2f, tempo %.0f BPM.",
                trackList, avgs.get("Energy"), avgs.get("Danceability"), avgs.get("Valence"), avgs.get("Avg BPM")
        );

        try {
            Map<String, Object> body = Map.of(
                    "contents", List.of(Map.of("parts", List.of(Map.of("text", prompt))))
            );

            Map response = geminiClient.post()
                    .uri("/gemini-2.0-flash:generateContent?key=" + geminiApiKey)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            List candidates = (List) response.get("candidates");
            Map content = (Map) ((Map) candidates.get(0)).get("content");
            List parts = (List) content.get("parts");
            return (String) ((Map) parts.get(0)).get("text");

        } catch (Exception e) {
            System.err.println("Gemini call failed: " + e.getMessage());
            return null;
        }
    }

    private double avg(java.util.stream.DoubleStream stream) {
        return Math.round(stream.average().orElse(0) * 100.0) / 100.0;
    }
}

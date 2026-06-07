package com.example.LoginSystem.Service.impl;

import com.example.LoginSystem.Model.Track.TrackDetailsEntity;
import com.example.LoginSystem.Model.Track.TrackEntity;
import com.example.LoginSystem.Repo.TrackDetailsRepository;
import com.example.LoginSystem.Repo.TrackRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MusicAnalysisService {
    private final TrackRepository trackRepository;
    private final TrackDetailsRepository trackDetailsRepository;
    private final List<TrackEntity> tracks;
    private final List<TrackDetailsEntity> tracksInfo;

    public MusicAnalysisService(TrackRepository trackRepository, TrackDetailsRepository trackDetailsRepository) {
        this.trackRepository = trackRepository;
        this.trackDetailsRepository = trackDetailsRepository;
        this.tracks = trackRepository.findAll();
        this.tracksInfo = trackDetailsRepository.findAll();
    }

    public List<TrackDetailsEntity> matchingTracks() {
        List<TrackDetailsEntity> filteredTracks = new ArrayList<>();

        List<String> trackSpotifyIds = tracksInfo.stream()
                .map(TrackDetailsEntity::getSpotifyId)
                .toList();

        for (TrackEntity track : tracks) {
            if (trackSpotifyIds.contains(track.getSpotifyId())) {
                tracksInfo.stream().filter(t -> t.getSpotifyId().equals(track.getSpotifyId()))
                        .findFirst().ifPresent(filteredTracks::add);
            }
        }
        return filteredTracks;
    }

    public List<String> TracksNamesBasedOnTracksIDs() {
        List<TrackEntity> TrackNames = new ArrayList<>();

        List<String> spotifyIDs = tracksInfo.stream()
                .map(TrackDetailsEntity::getSpotifyId)
                .toList();

        return tracks.stream()
                .filter(track -> spotifyIDs.contains(track.getSpotifyId()))
                .map(TrackEntity::getName)
                .toList();
    }


    public String calculateMoodStr() {
        double avgTempo = tracksInfo.stream()
                .mapToDouble(TrackDetailsEntity::getBpm)
                .average()
                .orElse(0.0);

        if(avgTempo > 0.6) {
            return "You like songs with positive features";
        }
        return "You listening to some sad songs";
    }

    public String calculateDanceabilityStr() {
        double avgDanceability = tracksInfo.stream()
                .mapToDouble(TrackDetailsEntity::getDanceability)
                .average()
                .orElse(0.0);

        if (avgDanceability > 0.7) {
            return "Your playlist is very good for parties";
        }
        else if (avgDanceability > 0.4) {
            return "Your music has a moderate groove to it";
        }
        return "You lean towards music that's better for listening than dancing";
    }

    public String calculateTempoStr() {
        double avgTempo = tracksInfo.stream()
                .mapToDouble(TrackDetailsEntity::getEnergy)
                .average()
                .orElse(0.0);

        if (avgTempo > 0.7) {
            return "You like very fast, high-tempo music";
        } else if (avgTempo > 0.5) {
            return "You enjoy upbeat music";
        }
        return "You prefer a steady, moderate pace in your music";
    }
}

package com.example.LoginSystem.Controller;


import com.example.LoginSystem.Model.Track.TrackDetailsEntity;
import com.example.LoginSystem.Model.Track.TrackEntity;
import com.example.LoginSystem.Repo.TrackRepository;
import com.example.LoginSystem.Service.impl.MusicAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import se.michaelthelin.spotify.SpotifyApi;

import java.util.List;

@Controller
public class MusicAnalysisController {

    @Autowired
    MusicAnalysisService musicAnalysisService;

    @Autowired
    TrackRepository trackRepository;

    @GetMapping("/MusicAnalysis")
    public String GetAnalysisPage(Model model) {
        try {
            List<TrackDetailsEntity> filteredTracks = musicAnalysisService.matchingTracks();
            //List<TrackEntity> allTracks = trackRepository.findAll();

            List<Float> valence = filteredTracks.stream()
                            .map(TrackDetailsEntity::getValence).toList();

            List<Float> energy = filteredTracks.stream()
                            .map(TrackDetailsEntity::getEnergy).toList();

            List<Float> danceability = filteredTracks.stream()
                            .map(TrackDetailsEntity::getDanceability).toList();

            List<String> trackNames = musicAnalysisService.TracksNamesBasedOnTracksIDs();

            model.addAttribute("mood", musicAnalysisService.calculateMoodStr());
            model.addAttribute("danceabilityStr", musicAnalysisService.calculateDanceabilityStr());
            model.addAttribute("tempo", musicAnalysisService.calculateTempoStr());

            model.addAttribute("trackNames",   trackNames);
            model.addAttribute("valence",valence);
            model.addAttribute("energy",energy);
            model.addAttribute("danceability",danceability);

        } catch (Exception e) {
            System.out.println("Error fetching from the DB");
        }
        return "/MusicAnalysis";
    }




}

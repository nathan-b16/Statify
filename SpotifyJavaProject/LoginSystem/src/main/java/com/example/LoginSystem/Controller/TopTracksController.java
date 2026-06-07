package com.example.LoginSystem.Controller;

import com.example.LoginSystem.Model.Track.TrackEntity;
import com.example.LoginSystem.Repo.TrackRepository;
import com.example.LoginSystem.Service.impl.TopItemServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.Collections;
import java.util.List;

@Controller
public class TopTracksController {

    @Autowired
    TopItemServiceImpl artistsService;

    @Autowired
    SpotifyApi spotifyApi;

    @GetMapping("/TopTracks")
    public String topTracks(HttpSession session, Model model)
    {
        String token = (String) session.getAttribute("accessToken");
        if (token == null) {
            return "redirect:/";
        }
        try {
            spotifyApi.setAccessToken(token);
            List<Track> topTrack = artistsService.getTopTracksOfAlTime(token);

            model.addAttribute("tracks", topTrack);
        }catch (Exception e){
            System.out.println("Error fetching data: " + e.getMessage());
            model.addAttribute("tracks", Collections.emptyList());

        }
        return "TopTracks";
    }

}
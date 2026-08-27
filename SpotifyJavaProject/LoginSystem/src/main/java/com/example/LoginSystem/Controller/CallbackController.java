package com.example.LoginSystem.Controller;

import com.example.LoginSystem.Auth.SpotifyAuthService;
import com.example.LoginSystem.Service.TopItemService;
import com.example.LoginSystem.Service.TrackDetailsService;
import com.example.LoginSystem.Service.TrackService;
import com.example.LoginSystem.Service.impl.RecentActivityService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.PlayHistory;

import java.util.Collections;
import java.util.List;


@Controller
public class CallbackController {

    @Autowired
    SpotifyAuthService authService;

    @Autowired
    TrackDetailsService trackDetailsService;

    @Autowired
    TopItemService topItemService;

   @Autowired
    SpotifyApi spotifyApi;

    @Autowired
    TrackService trackService;

    @Autowired
     RecentActivityService recentActivityService;

    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code, HttpSession session) {
        AuthorizationCodeCredentials credentials =  authService.exchangeCodeForToken(code);
        if(credentials != null) {
            String token = credentials.getAccessToken();

            session.setAttribute("accessToken", token);
            session.setAttribute("refreshToken", credentials.getRefreshToken());

            spotifyApi.setAccessToken(credentials.getAccessToken());
            spotifyApi.setRefreshToken(credentials.getRefreshToken());
            try {
                trackService.fetchAndSaveTracks(token);
            } catch (Exception e) {
                System.out.println("Error saving tracks: " + e.getMessage());
            }
            return "redirect:/mydashboard";
        }

        return "redirect:/";
    }

    @GetMapping("/mydashboard")
    public String mydashboard(HttpSession session, Model model) {
        String token = (String) session.getAttribute("accessToken");
        if (token == null) {
            return "redirect:/";
        }
        try {
            model.addAttribute("artists", topItemService.getTopArtists(token));
            model.addAttribute("tracks", topItemService.getTopTracks(token));
            List<PlayHistory> recentTracks = recentActivityService.getRecentlyPlayed(token);
            model.addAttribute("recentTracks", recentTracks);

            trackDetailsService.getTrackIds();

        } catch (Exception e) {
            System.out.println("Error fetching data: " + e.getMessage());

            model.addAttribute("artists", Collections.emptyList());
            model.addAttribute("tracks", Collections.emptyList());
        }
        return "mydashboard";
    }
}



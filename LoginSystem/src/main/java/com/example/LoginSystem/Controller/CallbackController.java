package com.example.LoginSystem.Controller;

import com.example.LoginSystem.Auth.SpotifyAuthService;
import com.example.LoginSystem.Service.SpotifyTokenService;
import com.example.LoginSystem.Service.TopItemService;
import com.example.LoginSystem.Service.TrackDetailsService;
import com.example.LoginSystem.Service.TrackService;
import com.example.LoginSystem.Service.impl.RecentActivityService;
import jakarta.servlet.http.HttpSession;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.PlayHistory;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Controller
public class CallbackController {

    private final SpotifyAuthService authService;
    private final TopItemService topItemService;
    private final TrackService trackService;
    private final RecentActivityService recentActivityService;
    private final SpotifyTokenService tokenService;

    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code, HttpSession session) throws IOException, ParseException, SpotifyWebApiException {
        AuthorizationCodeCredentials credentials =  authService.exchangeCodeForToken(code);
        if(credentials == null) {
            return "redirect:/error";
        }
        String token = credentials.getAccessToken();

            session.setAttribute("accessToken", token);
            session.setAttribute("refreshToken", credentials.getRefreshToken());
            session.setAttribute("expiresAt", Instant.now().plusSeconds(credentials.getExpiresIn()));

            //spotifyApi.setAccessToken(credentials.getAccessToken());
            //spotifyApi.setRefreshToken(credentials.getRefreshToken());

            try {
                trackService.fetchAndSaveTracks(token);
            } catch (Exception e) {
                System.out.println("Error saving tracks: " + e.getMessage());
            }

        return "redirect:/mydashboard";
    }

    @GetMapping("/mydashboard")
    public String mydashboard(HttpSession session, Model model) {
        String token = tokenService.getValidAccessToken(session);
        if (token == null) {
            return "redirect:/login";
        }
        try {
            model.addAttribute("artists", topItemService.getTopArtists(token));
            model.addAttribute("tracks", topItemService.getTopTracks(token));
            List<PlayHistory> recentTracks = recentActivityService.getRecentlyPlayed(token);
            model.addAttribute("recentTracks", recentTracks);

        } catch (Exception e) {
            System.out.println("Error fetching data: " + e.getMessage());
            model.addAttribute("artists", Collections.emptyList());
            model.addAttribute("tracks", Collections.emptyList());
        }
        return "mydashboard";
    }
}



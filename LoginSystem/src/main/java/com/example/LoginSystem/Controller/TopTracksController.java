package com.example.LoginSystem.Controller;

import com.example.LoginSystem.Auth.SpotifyAuthService;
import com.example.LoginSystem.Service.SpotifyTokenService;
import com.example.LoginSystem.Service.impl.TopItemServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class TopTracksController {

    private final TopItemServiceImpl artistsService;
    private final SpotifyAuthService authService;
    private final SpotifyTokenService tokenService;
    @GetMapping("/TopTracks")
    public String topTracks(HttpSession session, Model model)
    {
        String token = tokenService.getValidAccessToken(session);
        if (token == null) {
            return "redirect:/";
        }
        try {
            List<Track> topTrack = artistsService.getTopTracksOfAlTime(token);
            model.addAttribute("tracks", topTrack);
        }catch (Exception e){
            System.out.println("Error fetching data: " + e.getMessage());
            model.addAttribute("tracks", Collections.emptyList());

        }
        return "TopTracks";
    }

}
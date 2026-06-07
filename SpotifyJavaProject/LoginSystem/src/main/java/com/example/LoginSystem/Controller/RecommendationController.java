package com.example.LoginSystem.Controller;

import com.example.LoginSystem.DTO.RecommendationDTO;
import com.example.LoginSystem.Service.RecommendationService;
import com.example.LoginSystem.Service.impl.TopItemServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;

import java.util.List;


@Controller
public class RecommendationController {

    @Autowired
    RecommendationService recommendationService;

    @Autowired
    SpotifyApi spotifyApi;

    @GetMapping("/Recommendation")
    public String getRecommendations(HttpSession session,Model model) {
        String token = (String) session.getAttribute("accessToken");
        if (token == null) {
            return "redirect:/";
        }
        try{
            spotifyApi.setAccessToken(token);
            PlaylistSimplified[] userPlaylist = spotifyApi.getListOfCurrentUsersPlaylists().build().execute().getItems();

            List<RecommendationDTO> recommendations = recommendationService.TrackReccomendation();
            model.addAttribute("recommendations", recommendations);

            System.out.println(userPlaylist);
            model.addAttribute("playlists", userPlaylist);
        }
        catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        return "Recommendation";
    }
}

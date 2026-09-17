package com.example.LoginSystem.Controller;

import com.example.LoginSystem.Auth.SpotifyAuthService;
import com.example.LoginSystem.DTO.RecommendationDTO;
import com.example.LoginSystem.Service.RecommendationService;
import com.example.LoginSystem.Service.SpotifyTokenService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;

import java.util.List;


@RequiredArgsConstructor
@Controller
public class RecommendationController {


    private final SpotifyAuthService authService;
    private final RecommendationService recommendationService;
    private final SpotifyTokenService tokenService;

    @GetMapping("/Recommendation")
    public String getRecommendations(HttpSession session,Model model) {
        String token = tokenService.getValidAccessToken(session);
        if (token == null) {
            return "redirect:/";
        }
        try{
            PlaylistSimplified[] userPlaylist = authService.apiFor(token).getListOfCurrentUsersPlaylists().build().execute().getItems();
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

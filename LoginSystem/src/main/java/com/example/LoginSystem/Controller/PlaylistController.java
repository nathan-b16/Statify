package com.example.LoginSystem.Controller;

import com.example.LoginSystem.Auth.SpotifyAuthService;
import com.example.LoginSystem.Service.SpotifyTokenService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;


@RequiredArgsConstructor
@Controller
public class PlaylistController {

    private final SpotifyAuthService authService;
    private final SpotifyTokenService tokenService;

    @PostMapping("playlist/create")
    public String createPlaylist(@RequestParam String name ,HttpSession session,RedirectAttributes redirectAttributes)
    {
        String accessToken = tokenService.getValidAccessToken(session);
        if (accessToken == null) {
            return "redirect:/login";
        }
        try {
            SpotifyApi spotifyApi = authService.apiFor(accessToken);
            String userID = spotifyApi.getCurrentUsersProfile()
                    .build().execute().getId();
            spotifyApi.createPlaylist(userID, name).build().execute();
            redirectAttributes.addFlashAttribute("success", "Playlist " + name + " created!");
        } catch (Exception e) {
            throw new RuntimeException("Fauiled to create playlist: " + e.getMessage());
        }
        return "redirect:/Recommendation";
    }

    @PostMapping("/playlist/add")
    public String addPlaylist(@RequestParam String playlistId, @RequestParam String trackId,HttpSession session,RedirectAttributes redirectAttributes)
    {
        String accessToken = tokenService.getValidAccessToken(session);
        if(accessToken == null) {
            return "redirect:/login";
        }
        try{
            SpotifyApi spotifyApi = authService.apiFor(accessToken);
            String uri = "spotify:track:" + trackId;
            spotifyApi.addItemsToPlaylist(playlistId, new String[]{uri}).build().execute();
            redirectAttributes.addFlashAttribute("success", "Track added to playlist");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/Recommendation";
    }
}

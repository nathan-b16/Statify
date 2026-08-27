package com.example.LoginSystem.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;



@Controller
public class PlaylistController {

    @Autowired
    SpotifyApi spotifyApi;

    @PostMapping("playlist/create")
    public String createPlaylist(@RequestParam String name ,HttpSession session,RedirectAttributes redirectAttributes)
    {
        try {
            refreshToken(session);
            String userID = spotifyApi.getCurrentUsersProfile()
                    .build().execute().getId();
            spotifyApi.createPlaylist(userID, name).build().execute();
            redirectAttributes.addFlashAttribute("success", "Playlist " + name + " created!");
        }
        catch(RuntimeException e){
            if (e.getMessage().contains("not authenticated")) {
                return "redirect:/login";
            }
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/Recommendation";
    }

    @PostMapping("/playlist/add")
    public String addPlaylist(@RequestParam String playlistId, @RequestParam String trackId,HttpSession session,RedirectAttributes redirectAttributes)
    {
        try{
            refreshToken(session);
            String uri = "spotify:track:" + trackId;
            spotifyApi.addItemsToPlaylist(playlistId, new String[]{uri}).build().execute();
            redirectAttributes.addFlashAttribute("success", "Track added to playlist");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/Recommendation";
    }

    private void refreshToken(HttpSession session) throws Exception {
        String storedToken = (String)session.getAttribute("refreshToken");

        if(storedToken == null)
        {
            throw new RuntimeException("Not authenticated. Please log in");
        }
        spotifyApi.setRefreshToken(storedToken);

        AuthorizationCodeRefreshRequest refreshRequest = spotifyApi.authorizationCodeRefresh().build();
        AuthorizationCodeCredentials credentials = refreshRequest.execute();
        spotifyApi.setAccessToken(credentials.getAccessToken());
        session.setAttribute("accessToken", credentials.getAccessToken());

        if(credentials.getRefreshToken() != null)
        {
            spotifyApi.setRefreshToken(credentials.getRefreshToken());
            session.setAttribute("refreshToken", credentials.getRefreshToken());
        }
    }
}

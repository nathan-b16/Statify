package com.example.LoginSystem.Service;

import com.example.LoginSystem.Auth.SpotifyAuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class SpotifyTokenService {

    private final SpotifyAuthService authService;


    public String getValidAccessToken(HttpSession session) {
        String accessToken = (String) session.getAttribute("accessToken");
        String refreshToken = (String) session.getAttribute("refreshToken");
        Instant expiresAt = (Instant) session.getAttribute("expiresAt");

        if(accessToken == null || refreshToken == null) {
            return null;
        }

        boolean expired = (expiresAt == null || Instant.now().isAfter(expiresAt.minusSeconds(60)));
        if(!expired){
            return accessToken;
        }
        try {
            AuthorizationCodeCredentials refreshed = authService.exchangeCodeForToken(refreshToken);
            accessToken = refreshed.getAccessToken();
            session.setAttribute("accessToken", accessToken);
            session.setAttribute("expiresAt", Instant.now().plusSeconds(refreshed.getExpiresIn()));

            if(refreshed.getRefreshToken() != null){
                session.setAttribute("refreshToken", refreshed.getRefreshToken());
            }
            return accessToken;
        } catch (Exception e) {
            System.out.println("Error while refreshing Spotify token: " + e.getMessage());
            return null;
        }
    }
}

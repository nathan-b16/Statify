package com.example.LoginSystem.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;


@Service
public class SpotifyAuthService {

    @Autowired
    private SpotifyApi spotifyApi;

    public SpotifyApi getSpotifyApi() {
        if (spotifyApi.getAccessToken() == null) {
            getAuthorizationURL();
        }
        return spotifyApi;
    }

    public String getAuthorizationURL() {
        AuthorizationCodeUriRequest authorizationCodeUriRequest =
                spotifyApi.authorizationCodeUri()
                        .scope("playlist-read-private " +
                                "playlist-read-collaborative " +
                                "user-library-read " +
                                "playlist-modify-public " +
                                "playlist-modify-private " +
                                "user-read-recently-played " +
                                "user-read-private " +
                                "user-read-email " +
                                "user-top-read")
                        .show_dialog(true)
                        .build();

        return authorizationCodeUriRequest.execute().toString();
    }

    public AuthorizationCodeCredentials exchangeCodeForToken(String code) {
        AuthorizationCodeRequest request =
                spotifyApi.authorizationCode(code)
                        .build();
        try {
            return request.execute();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}

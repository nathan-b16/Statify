package com.example.LoginSystem.Auth;

import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;

import java.io.IOException;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class SpotifyAuthService {

    private final SpotifyApi.Builder spotifyApiBuilder;
    private volatile String appAccessToken;
    private volatile Instant appTokenExpiresAt;

    public String getAuthorizationURL() {
        AuthorizationCodeUriRequest request =
                spotifyApiBuilder.build()
                        .authorizationCodeUri()
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

        return request.execute().toString();
    }

    public AuthorizationCodeCredentials exchangeCodeForToken(String code) throws IOException, ParseException, SpotifyWebApiException {
        AuthorizationCodeRequest request = spotifyApiBuilder.build()
                .authorizationCode(code)
                .build();
        return request.execute();
    }

    public SpotifyApi apiFor(String accessToken) {
        SpotifyApi api = spotifyApiBuilder.build();
        api.setAccessToken(accessToken);
        return api;
    }

    public SpotifyApi appLevelApi() throws IOException, ParseException, SpotifyWebApiException {
        boolean expiringSoon = appAccessToken == null || appAccessToken == null ||
                Instant.now().isAfter(appTokenExpiresAt.minusSeconds(60));

        if(expiringSoon) {
            SpotifyApi api = spotifyApiBuilder.build();
            ClientCredentialsRequest request = api.clientCredentials().build();
            ClientCredentials credentials = request.execute();
            appAccessToken = credentials.getAccessToken();
            appTokenExpiresAt = Instant.now().plusSeconds(credentials.getExpiresIn());
        }

        SpotifyApi api = spotifyApiBuilder.build();
        api.setAccessToken(appAccessToken);
        return api;
    }
}

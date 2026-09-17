package com.example.LoginSystem.Service.impl;

import com.example.LoginSystem.Auth.SpotifyAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.PagingCursorbased;
import se.michaelthelin.spotify.model_objects.specification.PlayHistory;

import java.util.Arrays;
import java.util.List;


@RequiredArgsConstructor
@Service
public class RecentActivityService {


    private final SpotifyAuthService authService;

    public List<PlayHistory> getRecentlyPlayed(String accessToken)
    {
        try {
            PagingCursorbased<PlayHistory> paging = authService.apiFor(accessToken)
                    .getCurrentUsersRecentlyPlayedTracks()
                    .limit(1)
                    .build()
                    .execute();
            return Arrays.asList(paging.getItems());
        }catch (Exception e)
        {
            throw new RuntimeException("Failed to fetch recently played tracks: " + e.getMessage(), e);
        }
    }
}

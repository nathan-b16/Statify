package com.example.LoginSystem.Service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.PagingCursorbased;
import se.michaelthelin.spotify.model_objects.specification.PlayHistory;

import java.util.Arrays;
import java.util.List;


@Service
public class RecentActivityService {

    @Autowired
    SpotifyApi spotifyApi;

    public List<PlayHistory> getRecentlyPlayed(String accessToken)
    {
        try {
            PagingCursorbased<PlayHistory> paging = spotifyApi
                    .getCurrentUsersRecentlyPlayedTracks()
                    .limit(1)
                    .build()
                    .execute();
            return Arrays.asList(paging.getItems());
        }catch (Exception e)
        {
            throw  new RuntimeException();
        }
    }
}

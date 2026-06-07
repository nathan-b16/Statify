package com.example.LoginSystem.Service.impl;

import com.example.LoginSystem.Service.TopItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.*;
import se.michaelthelin.spotify.requests.IRequest;
import java.util.Arrays;
import java.util.List;

@Service
public class TopItemServiceImpl implements TopItemService {

    @Autowired
    SpotifyApi spotifyApi;

    private <T> List<T> fetchTopItems(IRequest<Paging<T>> request) {
        try {
            return Arrays.asList(request.execute().getItems());
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch Spotify data: " + e.getMessage());
        }
    }

    @Override
    public List<Artist> getTopArtists(String accessToken) {
        return fetchTopItems(
                spotifyApi.getUsersTopArtists()
                        .limit(5)
                        .time_range("short_term")
                        .build()
        );
    }


    @Override
    public List<Track> getTopTracks(String accessToken) {
        return fetchTopItems(
                spotifyApi.getUsersTopTracks()
                        .limit(5)
                        .time_range("short_term")
                        .build()
        );
    }

    @Cacheable(value = "TopTracksOfAlTime")
    public List<Track> getTopTracksOfAlTime(String accessToken) {
        return fetchTopItems(
                spotifyApi.getUsersTopTracks()
                        .limit(50)
                        .time_range("long_term")
                        .build()
        );
    }

    @Override
    public List<Track> getTopTracksForDB(String accessToken) {
        return fetchTopItems(
                spotifyApi.getUsersTopTracks()
                        .limit(20)
                        .time_range("short_term")
                        .build()
        );
    }

}

package com.example.LoginSystem.Service;

import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.List;

public interface TopItemService {

        List<Artist> getTopArtists(String accessToken);
        List<Track> getTopTracks(String accessToken);
        List<Track> getTopTracksForDB(String accessToken);
}

package com.example.LoginSystem.Service;

import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.IRequest;

import java.util.List;

public interface TopItemService {

        List<Artist> getTopArtists(String accessToken);
        List<Track> getTopTracks(String accessToken);
        List<Track> getTopTracksForDB(String accessToken);
}

package com.example.LoginSystem.Service;

import com.example.LoginSystem.Model.Track.TrackEntity;
import com.example.LoginSystem.Repo.TrackRepository;
import com.example.LoginSystem.Service.impl.TopItemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.model_objects.specification.*;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrackService {

    @Autowired
    TrackRepository trackRepository;

    @Autowired
    TopItemServiceImpl spotifyService;

    @Async
    public void fetchAndSaveTracks(String token) throws Exception
    {
        trackRepository.deleteAll();
        List<Track> tracks = spotifyService.getTopTracksForDB(token);

        List<TrackEntity> trackEntities = tracks.stream()
                .map(this::mapToEntity)
                .collect(Collectors.toList());
        trackRepository.saveAll(trackEntities);
    }

    private TrackEntity mapToEntity(Track track)
    {
        TrackEntity entity = new TrackEntity();
        entity.setSpotifyId(track.getId());
        entity.setName(track.getName());
        entity.setPopularity(track.getPopularity());
        entity.setDurationMs(track.getDurationMs());
        entity.setArtistName(extractArtistName(track));
        entity.setAlbumName(extractAlbumName(track));
        entity.setImageURL(extractImageUrl(track));
        return entity;
    }


    private String extractArtistName(Track track) {
        ArtistSimplified[] artists = track.getArtists();
        return (artists != null && artists.length > 0) ? artists[0].getName() : "Unknown Artist";
    }

    private String extractAlbumName(Track track) {
        AlbumSimplified album = track.getAlbum();
        return (album != null) ? album.getName() : "Unknown Album";
    }

    private String extractImageUrl(Track track) {
        AlbumSimplified album = track.getAlbum();
        if (album == null) return null;
        Image[] images = album.getImages();
        return (images != null && images.length > 0) ? images[0].getUrl() : null;
    }

}

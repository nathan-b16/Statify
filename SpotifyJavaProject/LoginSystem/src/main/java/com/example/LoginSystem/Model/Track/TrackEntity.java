package com.example.LoginSystem.Model.Track;

import jakarta.persistence.*;

@Entity
@Table(name = "tracks")
public class TrackEntity {

    @Id
    @Column(name = "spotify_id")
    private String spotifyId;

    @Column(name = "name")
    private String  name;

    @Column(name = "artist_name")
    private String  artistName;

    @Column(name = "album_name")
    private String  albumName;

    @Column(name = "popularity")
    private int  popularity;

    @Column(name = "duration_ms")
    private int  durationMs;

    @Column(name = "image_url")
    private String  imageURL;

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(int durationMs) {
        this.durationMs = durationMs;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public void setSpotifyId(String SpotifyId) {
        spotifyId = SpotifyId;
    }
}

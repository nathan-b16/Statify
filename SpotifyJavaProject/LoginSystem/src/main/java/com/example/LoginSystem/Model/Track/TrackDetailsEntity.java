package com.example.LoginSystem.Model.Track;


import jakarta.persistence.*;

@Entity
@Table(name = "track_details")
public class TrackDetailsEntity {

    @Id
    @Column(name = "spotify_id")
    private String spotifyId;

    @Column(name = "bpm")
    private Float bpm;

    @Column(name = "energy")
    private Float energy;

    @Column(name = "danceability")
    private Float danceability;

    @Column(name = "valence")
    private Float valence;

    @Column(name = "acousticness")
    private Float acousticness;

    @Column(name = "instrumentalness")
    private Float instrumentalness;

    @Column(name = "liveness")
    private Float liveness;

    @Column(name = "loudness")
    private Float loudness;

    @Column(name = "speechiness")
    private Float speechiness;

    @Column(name = "track_key")
    private Integer key;

    @Column(name = "track_mode")
    private Integer mode;

    public String getSpotifyId() {
        return spotifyId;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public Float getBpm() {
        return bpm;
    }

    public void setBpm(Float bpm) {
        this.bpm = bpm;
    }

    public Float getEnergy() {
        return energy;
    }

    public void setEnergy(Float energy) {
        this.energy = energy;
    }

    public Float getDanceability() {
        return danceability;
    }

    public void setDanceability(Float danceability) {
        this.danceability = danceability;
    }

    public Float getValence() {
        return valence;
    }

    public void setValence(Float valence) {
        this.valence = valence;
    }

    public Float getAcousticness() {
        return acousticness;
    }

    public void setAcousticness(Float acousticness) {
        this.acousticness = acousticness;
    }

    public Float getInstrumentalness() {
        return instrumentalness;
    }

    public void setInstrumentalness(Float instrumentalness) {
        this.instrumentalness = instrumentalness;
    }

    public Float getLiveness() {
        return liveness;
    }

    public void setLiveness(Float liveness) {
        this.liveness = liveness;
    }

    public Float getLoudness() {
        return loudness;
    }

    public void setLoudness(Float loudness) {
        this.loudness = loudness;
    }

    public Float getSpeechiness() {
        return speechiness;
    }

    public void setSpeechiness(Float speechiness) {
        this.speechiness = speechiness;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }
}

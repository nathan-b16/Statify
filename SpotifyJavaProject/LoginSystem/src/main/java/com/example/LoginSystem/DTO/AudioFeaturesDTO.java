package com.example.LoginSystem.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AudioFeaturesDTO {

    @JsonProperty("tempo")
    private Float tempo;

    @JsonProperty("energy")
    private Float energy;

    @JsonProperty("danceability")
    private Float danceability;

    @JsonProperty("valence")
    private Float valence;

    @JsonProperty("acousticness")
    private Float acousticness;

    @JsonProperty("instrumentalness")
    private Float instrumentalness;

    @JsonProperty("liveness")
    private Float liveness;

    @JsonProperty("loudness")
    private Float loudness;

    @JsonProperty("speechiness")
    private Float speechiness;

    @JsonProperty("key")
    private Integer key;

    @JsonProperty("mode")
    private Integer mode;

    public Float getTempo() {
        return tempo;
    }

    public void setTempo(Float tempo) {
        this.tempo = tempo;
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

package com.example.LoginSystem.DTO;

import se.michaelthelin.spotify.model_objects.specification.Artist;

import java.util.List;

public class RecommendationDTO {
    private String id;
    private String trackTitle;
    private List<Artist> artists;
    private String isrc;
    private String imageUrl;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getTrackTitle() {
        return trackTitle;
    }
    public void setTrackTitle(String trackTitle) {
        this.trackTitle = trackTitle;
    }

    public List<Artist> getArtists() {
        return artists;
    }
    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public String getIsrc() {
        return isrc;
    }
    public void setIsrc(String isrc) {
        this.isrc = isrc;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static class Artist {
        private String id;
        private String name;
        private String href;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }
    }

}

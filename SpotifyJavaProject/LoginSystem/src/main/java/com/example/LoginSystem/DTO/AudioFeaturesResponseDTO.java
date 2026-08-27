package com.example.LoginSystem.DTO;

import java.util.List;

public class AudioFeaturesResponseDTO {
    private List<AudioFeaturesDTO> content;

    public List<AudioFeaturesDTO> getContent() {
        return content;
    }

    public void setAudioFeaturesDTO(List<AudioFeaturesDTO> content) {
        this.content = content;
    }
}

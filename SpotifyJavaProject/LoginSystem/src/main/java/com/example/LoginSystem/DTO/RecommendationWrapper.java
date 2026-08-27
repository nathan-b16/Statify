package com.example.LoginSystem.DTO;


import java.util.List;

public class RecommendationWrapper {
    private List<RecommendationDTO> content;

    public List<RecommendationDTO> getContent()
    {
        return content;
    }

    public void setContent(List<RecommendationDTO> content) {
        this.content = content;
    }
}
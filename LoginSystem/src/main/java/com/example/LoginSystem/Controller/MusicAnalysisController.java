package com.example.LoginSystem.Controller;

import com.example.LoginSystem.Service.SpotifyTokenService;
import com.example.LoginSystem.Service.impl.MusicAnalysisService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class MusicAnalysisController {

    private final MusicAnalysisService musicAnalysisService;
    private final SpotifyTokenService tokenService;

    @GetMapping("/MusicAnalysis")
    public String GetAnalysisPage(HttpSession session, Model model) {

        String token = tokenService.getValidAccessToken(session);
        if (token == null) return "redirect:/login";

        model.addAttribute("tracks", musicAnalysisService.getTracks(token));
        model.addAttribute("audioStats", musicAnalysisService.getAudioAverages());

        try {
            model.addAttribute("insight", musicAnalysisService.generateInsight(token));
        } catch (Exception e) {
            System.err.println("Error generating insight: " + e.getMessage());
            model.addAttribute("insight", null);
        }

        return "/MusicAnalysis";
    }
}

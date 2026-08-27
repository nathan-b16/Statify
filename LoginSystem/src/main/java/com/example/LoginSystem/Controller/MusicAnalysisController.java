package com.example.LoginSystem.Controller;

import com.example.LoginSystem.Service.impl.MusicAnalysisService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MusicAnalysisController {

    @Autowired
    MusicAnalysisService musicAnalysisService;

    @GetMapping("/MusicAnalysis")
    public String GetAnalysisPage(HttpSession session, Model model) {
        String token = (String) session.getAttribute("accessToken");
        if (token == null) return "redirect:/";

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

package com.example.LoginSystem.Controller;

import com.example.LoginSystem.Auth.SpotifyAuthService;
import com.example.LoginSystem.Exception.AccountNotFoundException;
import io.netty.util.concurrent.CompleteFuture;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.apache.hc.core5.concurrent.CompletedFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@AllArgsConstructor
@Controller
public class AuthController {

    private final SpotifyAuthService authService;

    @GetMapping("/")
    public String index(HttpSession session) {
        if (session.getAttribute("accessToken") != null) {
            return "redirect:/mydashboard";
        }
        return "index";
    }


    @GetMapping("/login")
    public void login(HttpServletResponse response, HttpSession session) throws IOException, AccountNotFoundException {
        if (session.getAttribute("accessToken") != null) {
            response.sendRedirect("/mydashboard");
            return;
        }
        try {
            String authorizeURL = authService.getAuthorizationURL();
            response.sendRedirect(authorizeURL);
        }catch (Exception e) {
            response.sendRedirect("/error");
        }
    }
}

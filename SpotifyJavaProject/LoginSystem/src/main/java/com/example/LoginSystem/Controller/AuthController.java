package com.example.LoginSystem.Controller;

import com.example.LoginSystem.Auth.SpotifyAuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class AuthController {

    @Autowired
    private SpotifyAuthService authService;

    @GetMapping("/login")
    public void login(HttpServletResponse response) throws IOException {
        String authorizeURL = authService.getAuthorizationURL();
        response.sendRedirect(authorizeURL);
    }
}

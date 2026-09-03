package com._6.creatrove.auth.handler;

import com._6.creatrove.auth.jwt.JwtTokenProvider;
import com._6.creatrove.auth.oauth.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String token = jwtTokenProvider.createToken(oAuth2User.getUserId(), oAuth2User.getEmail());

        String targetUrl = "http://localhost:3000/oauth/callback?token=" + token;
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
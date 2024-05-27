package com.swyp.boardpick.handler;

import com.swyp.boardpick.domain.CustomOAuth2User;
import com.swyp.boardpick.domain.Uri;
import com.swyp.boardpick.provider.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String code = oAuth2User.getName();
        String token = jwtProvider.create(code);

        response.sendRedirect(Uri.MAIN_PAGE.getDescription() + "/auth/oauth-success?token=" + token);
//        response.sendRedirect(frontBaseUrl);
//        response.sendRedirect("http://localhost:3000" + "/auth/oauth-success?token=" + token);
//        response.sendRedirect("http://localhost:8080/" + token);
    }
}

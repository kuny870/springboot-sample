package com.wizvera.templet.config.OAuth2;

import com.wizvera.templet.model.SpOAuth2User;
import com.wizvera.templet.model.User;
import com.wizvera.templet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SpOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException
    {
        Object principal = authentication.getPrincipal();
        if(principal instanceof OidcUser) {
            // google
            SpOAuth2User oauth = SpOAuth2User.Provider.google.convert((OidcUser) principal);
            User user = userService.load(oauth);
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
            );


        }else if(principal instanceof OAuth2User) {
            // naver
            SpOAuth2User oauth = SpOAuth2User.Provider.google.convert((OAuth2User) principal);
            User user = userService.load(oauth);
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
            );
        }
        request.getRequestDispatcher("/").forward(request, response);
    }
}

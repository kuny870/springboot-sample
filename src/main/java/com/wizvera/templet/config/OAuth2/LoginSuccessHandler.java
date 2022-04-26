package com.wizvera.templet.config.OAuth2;

import com.wizvera.templet.exception.YouCannotLogin;
import com.wizvera.templet.model.User;
import com.wizvera.templet.repository.UserRepository;
import com.wizvera.templet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@Service
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
            ) throws IOException, ServletException
    {
        Optional<User> user = userRepository.findByUserId(authentication.getName());

        HttpSession session = request.getSession(true);
        session.setAttribute("id", user.get().getId());

        if(user.get().getDelYn().equals("Y")) {
            session.invalidate();
            response.sendRedirect("/login-error");
        }else {
            response.sendRedirect("/");
        }

    }

}

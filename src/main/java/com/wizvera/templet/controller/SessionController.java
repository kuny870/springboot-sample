package com.wizvera.templet.controller;

import com.wizvera.templet.model.SessionInfo;
import com.wizvera.templet.model.User;
import com.wizvera.templet.model.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.stream.Collectors;

@Controller
public class SessionController {

    @Autowired
    private SessionRegistry sessionRegistry;

    @GetMapping("/sessions")
    public ModelAndView sessions(ModelAndView mav){
        mav.addObject("sessionList",
                sessionRegistry.getAllPrincipals().stream().map(p->UserSession.builder()
                        .username(((User)p).getUsername())
                        .sessions(sessionRegistry.getAllSessions(p, false).stream().map(s->
                                SessionInfo.builder()
                                        .sessionId(s.getSessionId())
                                        .time(s.getLastRequest())
                                        .build())
                                .collect(Collectors.toList()))
                        .build()).collect(Collectors.toList()));
        mav.setViewName("sessionList");
        return mav;
    }

    @PostMapping("/session/expire")
    public String expireSession(@RequestParam String sessionId){
        SessionInformation sessionInformation = sessionRegistry.getSessionInformation(sessionId);
        if(!sessionInformation.isExpired()){
            sessionInformation.expireNow();
        }
        return "redirect:/sessions";
    }

    @GetMapping("/session-expired")
    public String sessionExpired(){
        return "/sessionExpired";
    }

}

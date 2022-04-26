package com.wizvera.templet.exception;

import org.springframework.security.access.AccessDeniedException;


public class YouCannotLogin extends AccessDeniedException {

    public YouCannotLogin() {
        super("회원이 존재하지 않습니다.");
    }


}

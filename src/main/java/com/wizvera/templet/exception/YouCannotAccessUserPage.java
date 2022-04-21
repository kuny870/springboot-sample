package com.wizvera.templet.exception;

import org.springframework.security.access.AccessDeniedException;


public class YouCannotAccessUserPage extends AccessDeniedException {

    public YouCannotAccessUserPage() {
        super("유저페이지 접근 거부");
    }


}

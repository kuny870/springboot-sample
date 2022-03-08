package com.wizvera.templet.config;

import org.springframework.stereotype.Component;

@Component
public class NameCheck {

    public boolean check(String name) {
        return name.equals("keon");
    }
}

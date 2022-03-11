package com.wizvera.templet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;

@SpringBootApplication
public class TempletApplication {

    CommonOAuth2Provider commonOAuth2Provider;

    public static void main(String[] args) {
        SpringApplication.run(TempletApplication.class, args);
    }

}

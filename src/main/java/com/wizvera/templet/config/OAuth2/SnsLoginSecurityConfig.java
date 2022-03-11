//package com.wizvera.templet.config.OAuth2;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
//@Configuration
//@EnableWebSecurity(debug = true)
////@EnableGlobalMethodSecurity(prePostEnabled = true)
//public class SnsLoginSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Autowired
//    private SpOAuth2SuccessHandler successHandler;
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        http
//                .oauth2Login(oauth2->oauth2
//                        .successHandler(successHandler)
//                )
//                ;
//    }
//
//}

package com.wizvera.templet.config;

import com.wizvera.templet.config.JWT.JwtCheckFilter;
import com.wizvera.templet.config.JWT.JwtTokenProvider;
import com.wizvera.templet.config.OAuth2.LoginSuccessHandler;
import com.wizvera.templet.config.OAuth2.SpOAuth2SuccessHandler;
import com.wizvera.templet.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import javax.annotation.Resource;
import javax.servlet.http.HttpSessionEvent;
import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.Collection;

@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    @Resource(name = "userService")
    private UserDetailsService userDetailsService;
    private final CustomAuthDetails customAuthDetails;
    private final DataSource dataSource;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private SpOAuth2SuccessHandler successHandler;

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    public SecurityConfig(UserService userService, CustomAuthDetails customAuthDetails, DataSource dataSource) {
        this.userService = userService;
        this.customAuthDetails = customAuthDetails;
        this.dataSource = dataSource;
    }

    // static ?????? ?????? ????????? ?????? ??????
    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/sessions", "session/expire", "/session-expired")
                .requestMatchers(
                PathRequest.toStaticResources().atCommonLocations()
        );
    }

    @Bean
    public RememberMeServices rememberMeServices(PersistentTokenRepository ptr) {
        PersistentTokenBasedRememberMeServices rememberMeServices = new PersistentTokenBasedRememberMeServices("jbcpCalendar", userDetailsService, tokenRepository());
        rememberMeServices.setParameter("obscure-remember-me");
        rememberMeServices.setCookieName("obscure-remember-me");
        return rememberMeServices;
    }

    // security config ??????
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .httpBasic().disable()
                .csrf().disable()       // csrf : ???????????? ?????? ??????
                .authorizeRequests(request-> {
                    request
                            .antMatchers("/v3/api-docs/**", "/swagger-resources/**", "/swagger-ui/**").permitAll()
                            .antMatchers("/", "/login", "/signup", "/auth", "/greeting/**").permitAll() // ?????? ?????? url
                            /*.antMatchers("/**").permitAll() // ?????? ?????? url*/
                            .antMatchers("/admin/**").hasRole("ADMIN")
                            .antMatchers("/user/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                            .mvcMatchers("/greeting/{name}").access("@nameCheck.check(#name)")
//                            .mvcMatchers("/user/page").hasRole("ADMIN")
                            .anyRequest().authenticated()       // ????????? ????????? ??????
//                            .accessDecisionManager(filAccessDecisionManager())    // accessDecisionManager : ?????? ?????????
                            ;
                        })
                        .formLogin(
//                                login->login.loginPage("/login")        // ????????? ?????????
//                                .permitAll()
//                                .defaultSuccessUrl("/", false)  // ????????? ?????? ??? ?????? ?????????  / alwaysUse ?????? true : ????????? url ?????? ?????? ??? ????????? ??? ?????? ????????? ??????
//                                .failureUrl("/login-error")             // ????????? ?????? ??? ?????? ?????????
//                                .authenticationDetailsSource(customAuthDetails)     // ?????? ?????? ?????? ??????
//                                .successHandler(loginSuccessHandler)
                        ).disable()
                        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .and()

                        .addFilterBefore(new JwtCheckFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                        .oauth2Login(oauth2->oauth2                 // OAuth2 ????????? (google, naver ?????????)
                                .successHandler(successHandler)
                        )
//                        .logout(logout->logout.logoutSuccessUrl("/"))       // ????????????
                        .exceptionHandling(error->
                                error
//                                        .accessDeniedPage("/access-denied")     // ?????? ?????? ??? ?????? ?????????
                                        .accessDeniedHandler(new CustomDeniedHandler())     // ?????? ?????? ????????? ????????? ??????
                                        .authenticationEntryPoint(new CustomEntryPoint())   // ????????? ????????? ?????? url ????????? ????????? ??????
                        )

                        // ????????? ????????????
                        .rememberMe()
                            .key("jbcpCalendar")           // ?????? ??? ??? ??????
                            .rememberMeParameter("jbcpCalendar-remember-me")
                            .rememberMeCookieName("jbcpCalendar-remember-me")
//                            .userDetailsService(userDetailsService)
//                            .tokenRepository(tokenRepository())     // ????????? ?????? ?????? ????????? ?????? DB ??????
                        .and()

                        // ?????? ??????
//                        .sessionManagement(
//                                s->s
////                                        .sessionCreationPolicy(p-> SessionCreationPolicy.STATELESS)
//                                        .sessionFixation(sessionFixationConfigurer -> sessionFixationConfigurer.changeSessionId())
//                                .maximumSessions(1)     // ?????? ?????? ?????? ??????
//                                .maxSessionsPreventsLogin(false)    // true : ?????? ?????? ??????, false : ?????? ?????? ??????
//                                .expiredUrl("/session-expired")     // ?????? ????????? ??? ????????? ??????
//                        )
                        ;

    }


    // ????????? ??? ???????????? ??????
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                // ?????? ?????????(userService)????????? UserDetailsService??? implements??????
                // loadUserByUsername() ??????????????? (????????? ??????)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher(){
            @Override
            public void sessionCreated(HttpSessionEvent event) {
                super.sessionCreated(event);
                System.out.printf("===>> [%s] ?????? ????????? %s \n", LocalDateTime.now(), event.getSession().getId());
            }

            @Override
            public void sessionDestroyed(HttpSessionEvent event) {
                super.sessionDestroyed(event);
                System.out.printf("===>> [%s] ?????? ????????? %s \n", LocalDateTime.now(), event.getSession().getId());
            }
        });
    }

    @Bean
    SessionRegistry sessionRegistry() {
        SessionRegistryImpl registry = new SessionRegistryImpl();
        return registry;
    }

    @Bean
    PersistentTokenRepository tokenRepository(){
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
        repository.setDataSource(dataSource);
        try{
            repository.removeUserTokens("1");
        }catch(Exception ex){
            repository.setCreateTableOnStartup(true);
        }
        return repository;
    }

    AccessDecisionManager filAccessDecisionManager() {
        return new AccessDecisionManager() {
            @Override
            public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
                throw new AccessDeniedException("?????? ??????");
//                return;
            }

            @Override
            public boolean supports(ConfigAttribute attribute) {
                return true;
            }

            @Override
            public boolean supports(Class<?> clazz) {
                return FilterInvocation.class.isAssignableFrom(clazz);
            }
        };
    }

    @Autowired
    private NameCheck nameCheck;

}

package com.wizvera.templet.config;

import com.wizvera.templet.config.OAuth2.SpOAuth2SuccessHandler;
import com.wizvera.templet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import javax.servlet.http.HttpSessionEvent;
import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.Collection;

@Configuration
@EnableWebSecurity(debug = true)
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter;

    private final UserService userService;
    private UserDetailsService userDetailsService;
    private final CustomAuthDetails customAuthDetails;
    private final DataSource dataSource;

    @Autowired
    private SpOAuth2SuccessHandler successHandler;

    public SecurityConfig(UserService userService, CustomAuthDetails customAuthDetails, DataSource dataSource) {
        this.userService = userService;
        this.customAuthDetails = customAuthDetails;
        this.dataSource = dataSource;
    }

    // 로그인 시 비밀번호 체크
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                // 해당 서비스(userService)에서는 UserDetailsService를 implements해서
                // loadUserByUsername() 구현해야함 (서비스 참고)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    // static 폴더 안의 파일들 접근 허용
    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/sessions", "session/expire", "/session-expired")
                .requestMatchers(
                PathRequest.toStaticResources().atCommonLocations()
        );
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher(){
            @Override
            public void sessionCreated(HttpSessionEvent event) {
                super.sessionCreated(event);
                System.out.printf("===>> [%s] 세션 생성됨 %s \n", LocalDateTime.now(), event.getSession().getId());
            }

            @Override
            public void sessionDestroyed(HttpSessionEvent event) {
                super.sessionDestroyed(event);
                System.out.printf("===>> [%s] 세션 만료됨 %s \n", LocalDateTime.now(), event.getSession().getId());
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
                throw new AccessDeniedException("접근 금지");
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

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .authorizeRequests(request-> {
                    request
                            .antMatchers("/", "/signup", "/user/save", "/auth", "/greeting/**").permitAll() // 모든 허용 url
                            .antMatchers("/admin/**").hasRole("ADMIN")
                            .antMatchers("/user/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                            .mvcMatchers("/greeting/{name}").access("@nameCheck.check(#name)")
//                            .mvcMatchers("/user-page").hasRole("ADMIN")
                            .anyRequest().authenticated()       // 권한이 있어야 허용
//                            .accessDecisionManager(filAccessDecisionManager())    // accessDecisionManager : 권한 위원회
                            ;
                        })
                        .formLogin(
                                login->login.loginPage("/login")        // 로그인 페이지
                                .permitAll()
                                .defaultSuccessUrl("/", false)  // 로그인 성공 시 이동 페이지  / alwaysUse 옵션 true : 페이지 url 접근 시도 후 로그인 시 해당 페이지 유지
                                .failureUrl("/login-error")             // 로그인 실패 시 이동 페이지
                                .authenticationDetailsSource(customAuthDetails)     // 권한 상세 정보 보기
                        )
                        .oauth2Login(oauth2->oauth2                 // OAuth2 로그인 (google, naver 로그인)
                                .successHandler(successHandler)
                        )
                        .logout(logout->logout.logoutSuccessUrl("/"))       // 로그아웃
                        .exceptionHandling(error->
                                error
//                                        .accessDeniedPage("/access-denied")     // 예외 발생 시 이동 페이지
                                        .accessDeniedHandler(new CustomDeniedHandler())     // 접근 권한 없을때 핸들러 작동
                                        .authenticationEntryPoint(new CustomEntryPoint())   // 로그인 안하고 상세 url 접근시 핸들러 작동
                        )

                // 로그인 유지하기
                .rememberMe()
                            .key("hello")           // 아무 키 값 고정
                            .userDetailsService(userDetailsService)
                            .tokenRepository(tokenRepository())     // 로그인 유지 토큰 저장을 위한 DB 생성
                        .and()
                // 세션 관리
                        .sessionManagement(
                                s->s
//                                        .sessionCreationPolicy(p-> SessionCreationPolicy.STATELESS)
                                        .sessionFixation(sessionFixationConfigurer -> sessionFixationConfigurer.changeSessionId())
                                .maximumSessions(1)     // 최대 유지 세션 갯수
                                .maxSessionsPreventsLogin(false)    // true : 기존 세션 유지, false : 신규 세션 유지
                                .expiredUrl("/session-expired")     // 세션 만료된 후 페이지 이동
                        )
                        ;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


//    @Bean
//    @Override
//    public UserDetailsService userDetailsService() {
//        UserDetails user =
//                User.withDefaultPasswordEncoder()
//                        .username("kuny87@naver.com")
//                        .password("1234")
//                        .roles("USER")
//                        .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }

}

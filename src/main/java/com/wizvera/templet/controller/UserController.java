package com.wizvera.templet.controller;

import com.wizvera.templet.config.JWT.JwtTokenProvider;
import com.wizvera.templet.model.User;
import com.wizvera.templet.model.response.Message;
import com.wizvera.templet.model.response.StatusEnum;
import com.wizvera.templet.repository.UserRepository;
import com.wizvera.templet.service.UserService;
import com.wizvera.templet.util.CryptoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static com.wizvera.templet.logging.LogMdcKey.DB_AUDIT_PREFIX;
import static com.wizvera.templet.logging.LogMdcKey.DB_LOGGING_PREFIX;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * 메인 페이지
     * @param mav
     * @param session
     * @return
     */
    @GetMapping("/")
    public ModelAndView main(ModelAndView mav, HttpSession session) {

        log.trace(">>>>>>>>>>>>>>>>>>>>>>>>>>> Trace Level 테스트");
        log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>> Debug Level 테스트");
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>> Info Level 테스트");
        log.warn(">>>>>>>>>>>>>>>>>>>>>>>>>>> Warn Level 테스트");
        log.error(">>>>>>>>>>>>>>>>>>>>>>>>>>> Error Level 테스트");

        mav.addObject("sessionId", session.getId());
        mav.setViewName("index");
        return mav;
    }

    /**
     * 로그인 페이지
     * @param mav
     * @return
     */
    @GetMapping("login")
    public ModelAndView login(ModelAndView mav) {
        mav.setViewName("loginForm");
        return mav;
    }

    /**
     * 로그인이 필요합니다 페이지
     * @param mav
     * @return
     */
    @GetMapping("login-required")
    public ModelAndView loginRequired(ModelAndView mav) {
        mav.setViewName("LoginRequired");
        return mav;
    }

    /**
     * 로그인 에러 페이지
     * @param mav
     * @return
     */
    @GetMapping("/login-error")
    public ModelAndView loginError(ModelAndView mav) {
        mav.addObject("loginError", true);
        mav.setViewName("loginError");
        return mav;
    }

    /**
     * 회원가입 페이지
     * @param mav
     * @return
     */
    @GetMapping("/signup")
    public ModelAndView signup(ModelAndView mav) {
        mav.setViewName("signup");
        return mav;
    }

    /**
     * 권한 디테일 정보 페이지
     * @return
     */
    @ResponseBody
    @GetMapping("/oauth2/auth")
    public Authentication auth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 접근 거부 페이지
     * @param mav
     * @return
     */
    @GetMapping("/access-denied")
    public ModelAndView accessDenied(ModelAndView mav) {
        mav.setViewName("AccessDenied");
        return mav;
    }

    /**
     * 접근 거부 페이지2
     * @param mav
     * @return
     */
    @GetMapping("/access-denied2")
    public ModelAndView accessDenied2(ModelAndView mav) {
        mav.setViewName("AccessDenied2");
        return mav;
    }

    /**
     * 회원 페이지
     * @param mav
     * @return
     */
    @GetMapping("/user/page")
    public ModelAndView userPage(ModelAndView mav) {
        mav.setViewName("UserPage");
        return mav;
    }

    /**
     * 회원가입
     * @param mav
     * @return
     */
    @PostMapping("/user/create")
    public ModelAndView signup(User user, ModelAndView mav) { // 회원 추가
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Optional<User> optionalUser = userRepository.findByUserId(user.getUserId());

        if(optionalUser.isPresent()){
            mav.setViewName("signupDuplicateId");
            return mav;
        }

        userRepository.save(User.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .password(user.getPassword())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .roles(user.getRoles())
                .build());
        mav.setViewName("index");
        return mav;
    }

//    @PostMapping("/user/create")
//    public void signup(@RequestBody User user){
//        userRepository.save(User.builder()
//                .email(user.getEmail())
//                .password(passwordEncoder.encode(user.getPassword()))
//                .name(user.getName())
//                .phoneNumber(user.getPhoneNumber())
//                .roles(Collections.singletonList("ROLE_USER"))
//                .build());
//
//    }

    /**
     * 회원정보 페이지
     * @param mav
     * @return
     */
    @GetMapping("/user/profile")
    public ModelAndView profile(ModelAndView mav, Authentication authentication) {
        Optional<User> user = userRepository.findByUserId(authentication.getName());
        mav.addObject("user", user.get());
        mav.setViewName("profile");
        return mav;
    }

    /**
     * 회원정보 수정
     * @param user
     * @return
     */
    @PostMapping("/user/update")
    public ResponseEntity<Message> update2(User user, HttpServletResponse response) throws IOException {
        userService.updateUser(user);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");

        response.sendRedirect("/");
        return ResponseEntity.ok(message);
    }

    /**
     * 회원 탈퇴
     * @param id
     * @return
     */
    @GetMapping("/user/delete")
    public ResponseEntity<Message> delete2(Long id, HttpServletRequest request) {
        userService.deleteUser(id);

        HttpSession session = request.getSession(true);
        session.invalidate();

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");

        return ResponseEntity.ok(message);
    }


    @PostMapping("/login")
    public void login(User user, HttpServletResponse response, ModelAndView mav, String remember) throws IOException {
        User member = userRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 ID 입니다."));
        if (!passwordEncoder.matches(user.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        String token = jwtTokenProvider.createToken(member.getUsername(), member.getRoles());

        String hToken = null;
        try {
            hToken = CryptoUtils.genHexSHA256(token, 1);
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            String errMsg = "error has occurred while generating the hashed-token.";
            log.info(DB_AUDIT_PREFIX);
            log.info(DB_LOGGING_PREFIX + "err[{}]", errMsg);
        }

        user.setToken(hToken);

        response.setHeader("X-AUTH-TOKEN", token);

        Cookie cookie = new Cookie("X-AUTH-TOKEN", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);

        response.sendRedirect("/");
    }

    @PostMapping("/logout")
    public void logout(HttpServletResponse response
                        , HttpServletRequest request
                        , @AuthenticationPrincipal User loginUser){

        if ( loginUser != null ) {
            loginUser.setToken(null);
            userRepository.save(loginUser);
            log.debug("[@DEBUG@]admin-info : {}", loginUser.getUserId());
        }

        Cookie cookie = new Cookie("X-AUTH-TOKEN", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @GetMapping("/info")
    public User getInfo(){
        Object details = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(details != null && !(details instanceof  String)) return new User((User) details);
        return null;
    }

}

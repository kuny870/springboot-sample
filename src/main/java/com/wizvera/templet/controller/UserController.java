package com.wizvera.templet.controller;

import com.wizvera.templet.model.User;
import com.wizvera.templet.model.response.Message;
import com.wizvera.templet.model.response.StatusEnum;
import com.wizvera.templet.repository.UserRepository;
import com.wizvera.templet.service.UserService;
import javassist.bytecode.DuplicateMemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

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
    public ModelAndView signup(User user, ModelAndView mav) throws DuplicateMemberException { // 회원 추가
        userService.save(user);
        mav.setViewName("index");
        return mav;
    }

    /**
     * 회원정보 페이지
     * @param mav
     * @return
     */
    @GetMapping("/user/profile")
    public ModelAndView profile(ModelAndView mav, Authentication authentication) {
        Optional<User> optionalUser = userRepository.findByUserId(authentication.getName());
        User user = optionalUser.get();
        mav.addObject("user", user);
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

}

package com.wizvera.templet.controller;

import com.wizvera.templet.model.User;
import com.wizvera.templet.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javassist.bytecode.DuplicateMemberException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Api(tags = {"유저 관련한 정보를 제공하는 Controller"})
@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserService userService;

    /**
     * OAuth2 테스트
     * @param user
     * @return
     */
    @ApiOperation(value = "OAuth2 테스트")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/oauth2/auth")
    public Object greeting3(@AuthenticationPrincipal Object user) {
        return user;
    }

    /**
     * JWT 테스트
     * @return
     */
    @ApiOperation(value = "JWT 테스트")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/greeting")
    public String greeting2() {
        return "hello";
    }

    /**
     * PreAuthorize 테스트
     * @param name
     * @return
     */
    @ApiOperation(value = "PreAuthorize 테스트")
    @PreAuthorize("@nameCheck.check(#name)")
    @GetMapping("/greeting/{name}")
    public String greeting1(@PathVariable String name) {
        return "hello";
    }

    /**
     * 메인페이지
     * @param mav
     * @param model
     * @param session
     * @return
     */
    @ApiOperation(value = "메인페이지")
    @GetMapping("/")
    public ModelAndView main(ModelAndView mav, Model model, HttpSession session) {

        logger.trace(">>>>>>>>>>>>>>>>>>>>>>>>>>> Trace Level 테스트");
        logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>> Debug Level 테스트");
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>> Info Level 테스트");
        logger.warn(">>>>>>>>>>>>>>>>>>>>>>>>>>> Warn Level 테스트");
        logger.error(">>>>>>>>>>>>>>>>>>>>>>>>>>> Error Level 테스트");

        mav.addObject("sessionId", session.getId());
        mav.setViewName("index");
        return mav;
    }

    /**
     * 로그인 페이지
     * @param mav
     * @return
     */
    @ApiOperation(value = "로그인 페이지")
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
    @ApiOperation(value = "로그인이 필요합니다 페이지")
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
    @ApiOperation(value = "로그인 에러 페이지")
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
    @ApiOperation(value = "회원가입 페이지")
    @GetMapping("/signup")
    public ModelAndView signup(ModelAndView mav) {
        mav.setViewName("signup");
        return mav;
    }

    /**
     * 권한 디테일 정보 페이지
     * @return
     */
    @ApiOperation(value = "권한 디테일 정보 페이지")
    @ResponseBody
    @GetMapping("/auth")
    public Authentication auth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 접근 거부 페이지
     * @param mav
     * @return
     */
    @ApiOperation(value = "접근 거부 페이지")
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
    @ApiOperation(value = "접근 거부 페이지2")
    @GetMapping("/access-denied2")
    public ModelAndView accessDenied2(ModelAndView mav) {
        mav.setViewName("AccessDenied2");
        return mav;
    }

    /**
     * 유저 페이지
     * @param mav
     * @return
     */
    @ApiOperation(value = "유저 페이지")
//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/user-page")
    public ModelAndView userPage(ModelAndView mav) {
//        if(true){
//            throw new YouCannotAccessUserPage();
//        }
        mav.setViewName("UserPage");
        return mav;
    }

    /**
     * 리턴 모든 유저 by JSON
     * @return
     */
    @ApiOperation(value = "리턴 모든 유저 by JSON")
    @Secured({"ROLE_USER", "RUN_AS_ADMIN"})
    @GetMapping("/user/userListByUser")
    public ResponseEntity userListByUser() {
        return ResponseEntity.ok(userService.getUsers());
    }


    /**
     * 회원 가입하기
     * @param user
     * @return
     */
    @ApiOperation(value = "회원 가입하는 POST 매핑 함수")
    @PostMapping("/user/save")
    public ModelAndView signup(User user) throws DuplicateMemberException { // 회원 추가
        ModelAndView mav = new ModelAndView();
        Long result = userService.save(user);
        if(result == 0L) {
            mav.setViewName("signupDuplicateId");
            mav.addObject("signupError", true);
        }else {
            mav.setViewName("index");
        }

        return mav;
    }

    /**
     * 회원정보 수정하기
     * @param user
     * @return
     */
    @ApiOperation(value = "회원 정보 수정하는 POST 매핑 함수")
    @PostMapping("/user/update")
    public ModelAndView update(User user) {  // 회원 수정
        userService.updateUser(user);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("main");
        return mav;
    }

    /**
     * 회원 탈퇴하기
     * @param user
     * @return
     */
    @ApiOperation(value = "회원 탈퇴하는 POST 매핑 함수")
    @PostMapping("/user/remove")
    public ModelAndView remove(User user) {  // 회원 탈퇴
        userService.removeUser(user);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("login");
        return mav;
    }




    /**
     * entityManager를 사용하여 직접 sql문 날리기 예제
     * @param username
     * @return
     */
//    @Transactional
//    @GetMapping("/searchUserName")
//    public String searchParamUser(@RequestParam(value = "username") String username) {
//        String sql = "SELECT u FROM UserInfo u WHERE u.username = :username";
//        List resultList = entityManager.createQuery(sql)
//                .setParameter("username", username)
//                .getResultList();
//        return resultList.toString();
//    }

}

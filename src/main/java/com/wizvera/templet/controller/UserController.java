package com.wizvera.templet.controller;

import com.wizvera.templet.model.User;
import com.wizvera.templet.service.UserService;
import javassist.bytecode.DuplicateMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {

    @Lazy
    private final UserService userService;

    // 테스트
    @PreAuthorize("@nameCheck.check(#name)")
    @GetMapping("/greeting/{name}")
    public String greeting(@PathVariable String name) {
        return "hello";
    }

    // 홈페이지
    @GetMapping("/")
    public ModelAndView main(ModelAndView mav, Model model, HttpSession session) {
        mav.addObject("sessionId", session.getId());
        mav.setViewName("index");
        return mav;
    }

    // 로그인 페이지
    @GetMapping("login")
    public ModelAndView login(ModelAndView mav) {
        mav.setViewName("loginForm");
        return mav;
    }

    // 로그인이 필요합니다 페이지
    @GetMapping("login-required")
    public ModelAndView loginRequired(ModelAndView mav) {
        mav.setViewName("LoginRequired");
        return mav;
    }

    // 로그인 에러 페이지
    @GetMapping("/login-error")
    public ModelAndView loginError(ModelAndView mav) {
        mav.addObject("loginError", true);
        mav.setViewName("loginError");
        return mav;
    }

    // 회원가입 페이지
    @GetMapping("/signup")
    public ModelAndView signup(ModelAndView mav) {
        mav.setViewName("signup");
        return mav;
    }

    // 권한 디테일 정보 페이지
    @ResponseBody
    @GetMapping("/auth")
    public Authentication auth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    // 접근 거부 페이지
    @GetMapping("/access-denied")
    public ModelAndView accessDenied(ModelAndView mav) {
        mav.setViewName("AccessDenied");
        return mav;
    }

    // 접근 거부 페이지2
    @GetMapping("/access-denied2")
    public ModelAndView accessDenied2(ModelAndView mav) {
        mav.setViewName("AccessDenied2");
        return mav;
    }

    // 유저 페이지
//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/user-page")
    public ModelAndView userPage(ModelAndView mav) {
//        if(true){
//            throw new YouCannotAccessUserPage();
//        }
        mav.setViewName("UserPage");
        return mav;
    }

    // 관리자 페이지
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/admin-page")
    public ModelAndView adminPage(ModelAndView mav) {
        mav.setViewName("AdminPage");
        return mav;
    }


    /**
     * 회원 전체 불러오기
     * @return
     */
//    @Secured({"SCHOOL_ADMIN"})
    @GetMapping("/admin/userList")
    public ResponseEntity getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @Secured({"ROLE_USER", "RUN_AS_ADMIN"})
    @GetMapping("/user/userListByUser")
    public ResponseEntity userListByUser() {
        return ResponseEntity.ok(userService.getUsers());
    }


    /**
     * 회원 가입하기
     * @param userInfo
     * @return
     */
    @PostMapping("/user/save")
    public ModelAndView signup(User userInfo) throws DuplicateMemberException { // 회원 추가
        ModelAndView mav = new ModelAndView();
        Long result = userService.save(userInfo);
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

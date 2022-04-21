package com.wizvera.templet.controller;

import com.wizvera.templet.model.DetectedProduct;
import com.wizvera.templet.model.User;
import com.wizvera.templet.model.response.Message;
import com.wizvera.templet.model.response.StatusEnum;
import com.wizvera.templet.repository.UserRepository;
import com.wizvera.templet.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javassist.bytecode.DuplicateMemberException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Api(tags = {"유저 관련한 정보를 제공하는 Controller"})
@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserService userService;

    private final UserRepository userRepository;


    /**
     * 회원 상세 불러오기
     * @return
     */
    @ApiOperation(value = "회원 상세 불러오기")
    @GetMapping("/user/detail")
    public ResponseEntity<Message> getUserDetail(User user) {

        Optional<User> optionalUser = userRepository.findByUserId(user.getUserId());

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");
        message.setData(optionalUser);

        return ResponseEntity.ok(message);
    }

    /**
     * 회원 가입하기
     * @param user
     * @return
     */
    @ApiOperation(value = "회원 가입하는 POST 매핑 함수")
    @PostMapping("/user/create")
    public ResponseEntity<Message> signup(User user) throws DuplicateMemberException { // 회원 추가

        userService.save(user);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");

        return ResponseEntity.ok(message);
    }


    /**
     * 회원정보 수정하기
     * @param user
     * @return
     */
    @ApiOperation(value = "회원 정보 수정하는 POST 매핑 함수")
    @PostMapping("/user/update")
    public ResponseEntity<Message> update(User user) {  // 회원 수정

        userService.updateUser(user);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");

        return ResponseEntity.ok(message);
    }

    /**
     * 회원 탈퇴하기
     * @param id
     * @return
     */
    @ApiOperation(value = "회원 탈퇴하는 POST 매핑 함수")
    @PostMapping("/user/delete")
    public ResponseEntity<Message> delete(Long id) {  // 회원 탈퇴

        userService.deleteUser(id);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");

        return ResponseEntity.ok(message);
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

    @PostMapping("/user/create2")
    public ModelAndView signup(User user, ModelAndView mav) throws DuplicateMemberException { // 회원 추가

        userService.save(user);

//        Message message = new Message();
//        message.setStatus(StatusEnum.OK);
//        message.setMessage("성공 코드");

        mav.setViewName("index");
        return mav;
    }

    /**
     * 권한 디테일 정보 페이지
     * @return
     */
    @ApiOperation(value = "권한 디테일 정보 페이지")
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
//    @ApiOperation(value = "리턴 모든 유저 by JSON")
//    @Secured({"ROLE_USER", "RUN_AS_ADMIN"})
//    @GetMapping("/user/userListByUser")
//    public ResponseEntity userListByUser() {
//        return ResponseEntity.ok(userService.getUsers());
//    }





    /**
     * 사진 다중 업로드 페이지
     * @return
     */
    @ApiOperation(value = "사진 업로드")
    @GetMapping("/picture-upload")
    public ModelAndView pictureUpload() {  // 사진 업로드
        ModelAndView mav = new ModelAndView();
        mav.setViewName("pictureUpload");
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

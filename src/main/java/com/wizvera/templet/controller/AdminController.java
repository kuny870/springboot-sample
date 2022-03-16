package com.wizvera.templet.controller;

import com.wizvera.templet.model.User;
import com.wizvera.templet.repository.UserRepository;
import com.wizvera.templet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Profile("local")
public class AdminController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserService userService;
    private final UserRepository userRepository;

    // 관리자 페이지
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/admin-page")
    public ModelAndView adminPage(ModelAndView mav) {
        mav.setViewName("AdminPage");
        return mav;
    }

    /**
     * 회원 전체 불러오기 json
     * @return
     */
    @GetMapping("/admin/users")
    public ResponseEntity getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    /**
     * 회원 전체 불러오기 modelAndView
     * @return
     */
    @GetMapping("/admin/userList")
    public ModelAndView getUserList(ModelAndView mav
            , @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum
            , @RequestParam(value = "size", defaultValue = "5") Integer size) {
        PageRequest pageRequest = PageRequest.of(pageNum-1, size);
        Page<User> userList = userService.getUserList(pageRequest);

        mav.addObject("page", userList);
        mav.setViewName("userList");
        return mav;
    }


    /**
     * 회원 승인하기
     * @return
     */
    @GetMapping("/admin/userApproval")
    public ResponseEntity<?> userApproval(
            @RequestParam("id") String id){

        boolean result = false;

        userRepository.updateUserApproval(id);

        result = true;

        return ResponseEntity.ok(result);
    }

    /**
     * 회원 승인 취소하기
     * @return
     */
    @GetMapping("/admin/userApprovalCancel")
    public ResponseEntity<?> userApprovalCancel(
            @RequestParam("id") String id){

        boolean result = false;

        userRepository.updateUserApprovalCancel(id);

        result = true;

        return ResponseEntity.ok(result);
    }


    /**
     * 회원 탈퇴시키기
     * @return
     */
    @GetMapping("/admin/userRemove")
    public ResponseEntity<?> userRemove(
            @RequestParam("id") String id) {

        boolean result = false;

        userRepository.updateUserRemove(id);

        result = true;

        return ResponseEntity.ok(result);
    }

    /**
     * 회원 복구시키기
     * @return
     */
    @GetMapping("/admin/userRestore")
    public ResponseEntity<?> userRestore(
            @RequestParam("id") String id) {

        boolean result = false;

        userRepository.updateUserRestore(id);

        result = true;

        return ResponseEntity.ok(result);
    }

}

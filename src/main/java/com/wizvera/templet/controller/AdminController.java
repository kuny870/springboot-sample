package com.wizvera.templet.controller;

import com.wizvera.templet.model.User;
import com.wizvera.templet.repository.UserRepository;
import com.wizvera.templet.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping
public class AdminController {

    private final UserService userService;
    private final UserRepository userRepository;

    /**
     * 관리자 페이지
     * @param mav
     * @return
     */
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/admin/page")
    public ModelAndView adminPage(ModelAndView mav) {
        mav.setViewName("AdminPage");
        return mav;
    }


    /**
     * 회원 전체 불러오기 By View
     * @return
     */
    @GetMapping("/admin/user/list")
    public ModelAndView getUserList(ModelAndView mav
            , @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum
            , @RequestParam(value = "size", defaultValue = "5") Integer size) {
        PageRequest pageRequest = PageRequest.of(pageNum-1, size, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<User> userList = userService.getUserList(pageRequest);

        mav.addObject("page", userList);
        mav.setViewName("userList");
        return mav;
    }



    /**
     * 회원 승인하기
     * @return
     */
    @GetMapping("/admin/user/approval")
    public ResponseEntity<?> userApproval2(
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
    @GetMapping("/admin/user/approval/cancel")
    public ResponseEntity<?> userApprovalCancel2(
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
    @GetMapping("/admin/user/delete")
    public ResponseEntity<?> userRemove2(
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
    @GetMapping("/admin/user/restore")
    public ResponseEntity<?> userRestore2(
            @RequestParam("id") String id) {

        boolean result = false;

        userRepository.updateUserRestore(id);

        result = true;

        return ResponseEntity.ok(result);
    }

}

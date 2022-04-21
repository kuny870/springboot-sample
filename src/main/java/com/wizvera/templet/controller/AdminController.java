package com.wizvera.templet.controller;

import com.wizvera.templet.model.User;
import com.wizvera.templet.model.response.Message;
import com.wizvera.templet.model.response.StatusEnum;
import com.wizvera.templet.repository.UserRepository;
import com.wizvera.templet.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

@Api(tags = {"관리자 관련한 정보를 제공하는 Controller"})
@RestController
@RequestMapping
@RequiredArgsConstructor
@Profile("local")
public class AdminController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserService userService;

    private final UserRepository userRepository;


    /**
     * 회원 전체 불러오기
     * @return
     */
    @ApiOperation(value = "회원 전체 불러오기")
    @GetMapping("/admin/user/list")
    public ResponseEntity<Message> getUsers(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum
            , @RequestParam(value = "size", defaultValue = "10") Integer size) {

        PageRequest pageRequest = PageRequest.of(pageNum-1, size);
        Page<User> userList = userService.getUserList(pageRequest);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");
        message.setData(userList);

        return ResponseEntity.ok(message);
    }

    /**
     * 회원 승인하기
     * @return
     */
    @ApiOperation(value = "회원 승인하기")
    @PostMapping("/admin/user/approval")
    public ResponseEntity<Message> userApproval(String id){

        userRepository.updateUserApproval(id);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");

        return ResponseEntity.ok(message);
    }

    /**
     * 회원 승인 취소하기
     * @return
     */
    @ApiOperation(value = "회원 승인 취소하기")
    @PostMapping("/admin/user/approvalCancel")
    public ResponseEntity<Message> userApprovalCancel(String id){

        userRepository.updateUserApprovalCancel(id);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");

        return ResponseEntity.ok(message);
    }


    /**
     * 회원 탈퇴시키기
     * @return
     */
    @ApiOperation(value = "회원 탈퇴 시키기")
    @PostMapping("/admin/user/delete")
    public ResponseEntity<Message> userRemove(String id) {

        userRepository.updateUserRemove(id);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");

        return ResponseEntity.ok(message);
    }

    /**
     * 회원 복구시키기
     * @return
     */
    @ApiOperation(value = "회원 복구 시키기")
    @PostMapping("/admin/user/restore")
    public ResponseEntity<Message> userRestore(String id) {

        userRepository.updateUserRestore(id);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");

        return ResponseEntity.ok(message);
    }

















    /**
     * 관리자 페이지
     * @param mav
     * @return
     */
    @ApiOperation(value = "관리자 페이지")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/admin-page")
    public ModelAndView adminPage(ModelAndView mav) {
        mav.setViewName("AdminPage");
        return mav;
    }


    /**
     * 회원 전체 불러오기 By View
     * @return
     */
    @ApiOperation(value = "회원 전체 불러오기 By View")
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
    @ApiOperation(value = "회원 승인하기")
    @GetMapping("/admin/userApproval")
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
    @ApiOperation(value = "회원 승인 취소하기")
    @GetMapping("/admin/userApprovalCancel")
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
    @ApiOperation(value = "회원 탈퇴 시키기")
    @GetMapping("/admin/userRemove")
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
    @ApiOperation(value = "회원 복구 시키기")
    @GetMapping("/admin/userRestore")
    public ResponseEntity<?> userRestore2(
            @RequestParam("id") String id) {

        boolean result = false;

        userRepository.updateUserRestore(id);

        result = true;

        return ResponseEntity.ok(result);
    }

}

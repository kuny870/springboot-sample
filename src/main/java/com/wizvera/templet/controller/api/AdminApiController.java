package com.wizvera.templet.controller.api;

import com.wizvera.templet.model.User;
import com.wizvera.templet.model.response.Message;
import com.wizvera.templet.model.response.StatusEnum;
import com.wizvera.templet.repository.UserRepository;
import com.wizvera.templet.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Api(tags = { "102. 관리자 관련 정보" })
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping(value = "/api/admin")
public class AdminApiController {

    private final UserService    userService;
    private final UserRepository userRepository;

    @ApiOperation(value = "회원 전체 불러오기", notes = "가입한 전체 회원을 불러온다.")
    @GetMapping("/user/list")
    public ResponseEntity<Message> getUsers(
            @ApiParam(value = "페이지번호", required = false) @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @ApiParam(value = "폐이지당 데이터 개수", required = false) @RequestParam(value = "size", defaultValue = "10") Integer size) {

        PageRequest pageRequest = PageRequest.of(pageNum-1, size);
        Page<User> userList = userService.getUserList(pageRequest);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");
        message.setData(userList);

        return ResponseEntity.ok(message);
    }

    @ApiOperation(value = "회원승인", notes = "정회원 권한을 부여한다.")
    @PostMapping("/user/approval")
    public ResponseEntity<Message> userApproval(
            @ApiParam(value = "회원 아이디", required = false) String id){

        userRepository.updateUserApproval(id);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");

        return ResponseEntity.ok(message);
    }

    @ApiOperation(value = "회원승인 취소", notes = "정회원 권한을 박탈한다.")
    @PostMapping("/user/approval/cancel")
    public ResponseEntity<Message> userApprovalCancel(
            @ApiParam(value = "회원 아이디", required = false) String id){

        userRepository.updateUserApprovalCancel(id);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");

        return ResponseEntity.ok(message);
    }

    @ApiOperation(value = "회원강퇴", notes = "회원을 강퇴 시킨다.")
    @PostMapping("/user/delete")
    public ResponseEntity<Message> userRemove(
            @ApiParam(value = "회원 아이디", required = false) String id) {

        userRepository.updateUserRemove(id);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");

        return ResponseEntity.ok(message);
    }

    @ApiOperation(value = "회원복구", notes = "강퇴시킨 회원을 복구한다.")
    @PostMapping("/user/restore")
    public ResponseEntity<Message> userRestore(
            @ApiParam(value = "회원 아이디", required = false) String id) {

        userRepository.updateUserRestore(id);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");

        return ResponseEntity.ok(message);
    }


}


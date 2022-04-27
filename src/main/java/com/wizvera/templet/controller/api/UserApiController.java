package com.wizvera.templet.controller.api;

import com.wizvera.templet.model.User;
import com.wizvera.templet.model.response.Message;
import com.wizvera.templet.model.response.StatusEnum;
import com.wizvera.templet.repository.UserRepository;
import com.wizvera.templet.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javassist.bytecode.DuplicateMemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@Api(tags = { "100. 유저 관련 정보"})
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping(value = "/api/user")
public class UserApiController {

    private final UserService    userService;
    private final UserRepository userRepository;

    @ApiOperation(value = "회원 상세 정보 불러오기", notes = "회원의 상세 정보를 불러온다.")
    @GetMapping("/detail")
    public ResponseEntity<Message> getUserDetail(
            @ApiParam(value = "회원정보", required = true) User user) {

        Optional<User> optionalUser = userRepository.findByUserId(user.getUserId());

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");
        message.setData(optionalUser);

        return ResponseEntity.ok(message);
    }

    @ApiOperation(value = "회원가입", notes = "회원 정보를 등록한다.")
    @PostMapping("/create")
    public ResponseEntity<Message> signup(
            @ApiParam(value = "회원정보", required = true) User user) throws DuplicateMemberException { // 회원 추가

        userService.save(user);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");

        return ResponseEntity.ok(message);
    }

    @ApiOperation(value = "회원정보 수정", notes = "회원 정보를 수정한다.")
    @PostMapping("/update")
    public ResponseEntity<Message> update(
            @ApiParam(value = "회원정보", required = true) User user) {  // 회원 수정

        userService.updateUser(user);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");

        return ResponseEntity.ok(message);
    }

    @ApiOperation(value = "회원탈퇴", notes = "회원 정보를 삭제한다.")
    @PostMapping("/delete")
    public ResponseEntity<Message> delete(
            @ApiParam(value = "회원 아이디", required = true) Long id,
            HttpServletRequest request) {  // 회원 탈퇴

        userService.deleteUser(id);

        HttpSession session = request.getSession(true);
        session.invalidate();

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");

        return ResponseEntity.ok(message);
    }




}

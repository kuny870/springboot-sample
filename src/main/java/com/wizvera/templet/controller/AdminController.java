package com.wizvera.templet.controller;

import com.wizvera.templet.model.User;
import com.wizvera.templet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AdminController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserService userService;

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

}

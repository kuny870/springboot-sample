package com.wizvera.templet.controller;

import com.wizvera.templet.model.User;
import com.wizvera.templet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping(value = "/api/user")
@RequiredArgsConstructor
public class UserController {

    @PersistenceContext
    private EntityManager entityManager;

    private final UserService userService;

    /**
     * 사용자 전체 불러오기
     * @return
     */
    @GetMapping("/getAll")
    public ResponseEntity getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    /**
     * 사용자 한명 불러오기
     * @param loginId
     * @return
     */
    @GetMapping("/getOne/{loginId}")
    public ResponseEntity getUser(@PathVariable String loginId) {
        return ResponseEntity.ok(userService.getUser(loginId));
    }

    /**
     * 사용자 저장하기
     * @param saveUser
     * @return
     */
    @PostMapping("/save")
    public ResponseEntity<User> insertUser(@RequestBody User saveUser) {
        return ResponseEntity.ok(userService.insertUser(saveUser));
    }

    /**
     * 사용자 수정하기
     * @param updateUser
     * @return
     */
    @PostMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User updateUser) {
        return ResponseEntity.ok(userService.updateUser(updateUser));
    }


    /**
     * entityManager를 사용하여 직접 sql문 날리기
     * @param name
     * @return
     */
    @Transactional
    @GetMapping("/searchUserName")
    public String searchParamUser(@RequestParam(value = "name") String name) {
        String sql = "SELECT u FROM User u WHERE u.name = :name";
        List resultList = entityManager.createQuery(sql)
                .setParameter("name", name)
                .getResultList();
        return resultList.toString();
    }



}

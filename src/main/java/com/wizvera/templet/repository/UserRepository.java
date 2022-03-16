package com.wizvera.templet.repository;

import com.wizvera.templet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

//    @Query(value = "select * from user where username = :username", nativeQuery=true)
//    User findByUsername(@Param("username") String username);

    Optional<User> findByEmail(String username);

//    @Modifying
//    @Query("UPDATE user u SET u.delYn = 'Y' WHERE u.loginId = :loginId")
//    void passwdFail(@Param("loginId") String loginId);

}

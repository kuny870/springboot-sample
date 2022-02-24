package com.wizvera.templet.repository;

import com.wizvera.templet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select * from user where login_id = :loginId", nativeQuery=true)
    User findByLoginId(@Param("loginId") String loginId);

//    @Modifying
//    @Query("UPDATE user u SET u.delYn = 'Y' WHERE u.loginId = :loginId")
//    void passwdFail(@Param("loginId") String loginId);

}

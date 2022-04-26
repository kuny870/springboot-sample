package com.wizvera.templet.repository;

import com.wizvera.templet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    @Query(value = "select * from user where role = 'ROLE_USER' LIMIT :pageNum, :pageSize", nativeQuery = true)
    List<User> findByRoleUser(int pageNum, int pageSize);

    Optional<User> findByUserId(String userId);

    Optional<User> findByEmail(String userEmail);

    @Modifying
    @Transactional
    @Query(value = "update user set approval = 'Y' where id = :id", nativeQuery = true)
    void updateUserApproval(@Param("id") String id);

    @Modifying
    @Transactional
    @Query(value = "update user set approval = 'N' where id = :id", nativeQuery = true)
    void updateUserApprovalCancel(@Param("id") String id);

    @Modifying
    @Transactional
    @Query(value = "update user set del_yn = 'Y' where id = :id", nativeQuery = true)
    void updateUserRemove(@Param("id") String id);

    @Modifying
    @Transactional
    @Query(value = "update user set del_yn = 'N' where id = :id", nativeQuery = true)
    void updateUserRestore(@Param("id") String id);

}

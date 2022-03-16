package com.wizvera.templet.repository;

import com.wizvera.templet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface ScheduleRepository extends JpaRepository<User, Long> {

    @Modifying
    @Transactional
    @Query(value = "update user set status = 1 where (date_format(now(), '%Y%m%d') - date_format(modified_date, '%Y%m%d')) > 10000 and status = 0", nativeQuery = true)
    void userExpiring();

}

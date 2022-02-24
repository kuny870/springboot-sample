package com.wizvera.templet.repository;

import com.wizvera.templet.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}

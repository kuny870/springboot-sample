package com.wizvera.templet.repository;

import com.wizvera.templet.model.SpOAuth2User;
import com.wizvera.templet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpOAuth2UserRepository extends JpaRepository<SpOAuth2User, String> {


}

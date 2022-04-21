package com.wizvera.templet.repository;

import com.wizvera.templet.model.DetectingSearchKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetectingSearchKeywordRepository extends JpaRepository<DetectingSearchKeyword, Long> {

    void deleteByDetectingProductId(Long id);
}

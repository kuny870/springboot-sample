package com.wizvera.templet.repository;

import com.wizvera.templet.model.DetectingProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetectingProductImageRepository extends JpaRepository<DetectingProductImage, Long> {

    void deleteByDetectingProductId(Long id);

}

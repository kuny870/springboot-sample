package com.wizvera.templet.repository;

import com.wizvera.templet.model.DetectingProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

public interface DetectingProductRepository extends JpaRepository<DetectingProduct, Long> {

    @Modifying
    @Transactional
    @Query(value = "update detecting_product set del_yn = 'Y' where id = :id", nativeQuery = true)
    void updateDetectingRemove(@Param("id") String id);

    @Modifying
    @Transactional
    @Query(value = "update detecting_product set del_yn = 'N' where id = :id", nativeQuery = true)
    void updateDetectingRestore(@Param("id") String id);

    Optional<DetectingProduct> findAllById(Long id);
}

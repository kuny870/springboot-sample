package com.wizvera.templet.repository;

import com.wizvera.templet.model.DetectedProduct;
        import net.minidev.json.JSONObject;
        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.data.jpa.repository.Query;

public interface DetectedProductRepository extends JpaRepository<DetectedProduct, Long> {

    @Query(value = "select extra from detected_product where id = :id", nativeQuery = true)
    JSONObject findData(Long id);

}

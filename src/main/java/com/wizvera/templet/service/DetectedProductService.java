package com.wizvera.templet.service;

import com.wizvera.templet.model.DetectedProduct;
import com.wizvera.templet.model.DetectedProductReview;
import com.wizvera.templet.repository.DetectedProductRepository;
import com.wizvera.templet.repository.DetectedProductReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class DetectedProductService {

    private final DetectedProductRepository detectedProductRepository;

    private final DetectedProductReviewRepository detectedProductReviewRepository;


    /**
     * 가품탐지된 상품 전체 불러오기 Paging
     * @return
     */
    public Page<DetectedProduct> getDetectedProductList(PageRequest pageRequest) {
        return detectedProductRepository.findAll(pageRequest);
    }


    /**
     * 가품탐지된 제품 등록
     * @param detectedProduct
     * @return
     */
    @Transactional
    public void regist(DetectedProduct detectedProduct) {

        DetectedProduct dp = detectedProductRepository.save(detectedProduct);

        for(int i = 0; i < detectedProduct.getComment().size(); i++){
            DetectedProductReview dpr = new DetectedProductReview();
            dpr.setDetectedProductId(dp.getId());
            dpr.setComment(detectedProduct.getComment().get(i).toString());
            detectedProductReviewRepository.save(dpr);
        }

    }


    /**
     * 가품탐지된 제품 삭제
     * @param id
     * @return
     */
    public DetectedProduct delete(Long id) {

        Optional<DetectedProduct> optionalDetectedProduct = detectedProductRepository.findById(id);
        DetectedProduct getDetectedProduct = optionalDetectedProduct.get();
        getDetectedProduct.setDelYn("Y");

        return detectedProductRepository.save(getDetectedProduct);
    }

}


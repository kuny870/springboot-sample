package com.wizvera.templet.controller;

import com.wizvera.templet.model.DetectedProduct;
import com.wizvera.templet.model.DetectingProduct;
import com.wizvera.templet.model.response.Message;
import com.wizvera.templet.model.response.StatusEnum;
import com.wizvera.templet.repository.DetectedProductRepository;
import com.wizvera.templet.repository.DetectingProductRepository;
import com.wizvera.templet.service.DetectedProductService;
import com.wizvera.templet.service.DetectingProductService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping
public class DetectController {

    private final DetectingProductService detectingProductService;
    private final DetectedProductService detectedProductService;
    private final DetectingProductRepository detectingProductRepository;
    private final DetectedProductRepository detectedProductRepository;

    /**
     * 가품탐지할 제품 등록 페이지
     * @return
     */
    @GetMapping("/detecting/product/regist")
    public ModelAndView detectingProductRegist2() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("detectingRegist");
        return mav;
    }

    /**
     * 가품탐지 제품 등록
     * @return
     */
    @PostMapping(value = "/detecting/product/regist")
    public ModelAndView detectingProductRegist3(
            DetectingProduct detectingProduct
            , MultipartHttpServletRequest mhsr
            , ModelAndView mav) throws IOException {

        detectingProductService.regist(detectingProduct, mhsr);

        mav.setViewName("UserPage");
        return mav;
    }

    /**
     * 가품탐지할 제품 목록
     * @return
     */
    @GetMapping(value = "/detecting/product/list")
    public ModelAndView detectedProductList2(
            ModelAndView mav
            , @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum
            , @RequestParam(value = "size", defaultValue = "10") Integer size) {

        PageRequest pageRequest = PageRequest.of(pageNum-1, size);
        Page<DetectingProduct> detectingProductList = detectingProductService.getDetectingProductList(pageRequest);

        mav.addObject("page", detectingProductList);
        mav.setViewName("detectingList");
        return mav;
    }

    /**
     * 제품 삭제 시키기
     * @return
     */
    @GetMapping("/detecting/product/delete")
    public ResponseEntity<Message> detectedProductDelete2(String id) {

        detectingProductRepository.updateDetectingRemove(id);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");

        return ResponseEntity.ok(message);
    }

    /**
     * 제품 복구 시키기
     * @return
     */
    @GetMapping("/detecting/product/restore")
    public ResponseEntity<Message> detectingProductRestore2(String id) {

        detectingProductRepository.updateDetectingRestore(id);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");

        return ResponseEntity.ok(message);
    }

    /**
     * 가품탐지할 제품 상세 불러오기
     * @param id
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/detecting/product/detail")
    public ModelAndView detectingProductDetail2(Long id, ModelAndView mav) {

        Optional<DetectingProduct> optionalDetectingProduct = detectingProductRepository.findById(id);
        DetectingProduct dp = optionalDetectingProduct.get();

        mav.addObject("dp", dp);
        mav.setViewName("detectingDetail");
        return mav;
    }

    /**
     * 가품탐지할 제품 수정하기
     * @param detectingProduct
     * @return
     * @throws IOException
     */
    @PostMapping(value = "/detecting/product/update", consumes = {"multipart/form-data"})
    public ResponseEntity<Message> detectingProductUpdate2(
            DetectingProduct detectingProduct
            , MultipartHttpServletRequest mhsr
            , HttpServletResponse response) throws IOException {

        Optional<DetectingProduct> tempDp = detectingProductRepository.findById(detectingProduct.getId());
        detectingProduct.setCreatedDate(tempDp.get().getCreatedDate());

        detectingProductService.update(detectingProduct, mhsr);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");

        response.sendRedirect("/detecting/product/list");
        return ResponseEntity.ok(message);
    }

    /**
     * 가품탐지된 제품 등록하기
     * @param detectedProduct
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/detected/product/regist")
    public ResponseEntity<Message> detectedProductRegist2(
            Long id, DetectedProduct detectedProduct) {

        Optional<DetectingProduct> dp = detectingProductRepository.findAllById(id);

        detectedProduct.setDetectingProductId(id);
        detectedProduct.setProductName(dp.get().getProductName());
        detectedProduct.setPageId("1234");
        detectedProduct.setPageAddress("https://www.11st.co.kr/?jaehuid=200010621");
        detectedProduct.setPageMainImage("https://akgolf.speedgabia.com/webhard/auction/accessory/cap/2021/21_07_nike_best_cap.jpg");
        detectedProduct.setPageSite("11번가");
        detectedProduct.setSeller("최건희");
        detectedProduct.setOriginPrice(20000);
        detectedProduct.setPrice(10000);
        detectedProduct.setAverageScore("4");
        detectedProduct.setReviewCount("10");
        detectedProduct.setImitationPercentage("90");
        ArrayList arrayList = new ArrayList();
        arrayList.add("진짜네~~");
        arrayList.add("진짜 맞음?");
        detectedProduct.setComment(arrayList);

        detectedProductService.regist(detectedProduct);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");

        return ResponseEntity.ok(message);
    }

    /**
     * 가품탐지된 제품 전체 불러오기
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/detected/product/list")
    public ModelAndView detectedProductList2(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum
            , @RequestParam(value = "size", defaultValue = "10") Integer size
            , ModelAndView mav) {

        PageRequest pageRequest = PageRequest.of(pageNum-1, size);
        Page<DetectedProduct> detectedProductList = detectedProductService.getDetectedProductList(pageRequest);

        mav.addObject("page", detectedProductList);
        mav.setViewName("detectedList");
        return mav;
    }

    /**
     * 가품탐지된 제품 상세 불러오기
     * @param id
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/detected/product/detail")
    public ModelAndView detectedProductDetail2(Long id, ModelAndView mav) {

        Optional<DetectedProduct> optionalDetectedProduct = detectedProductRepository.findById(id);

        mav.addObject("dp", optionalDetectedProduct.get());
        mav.setViewName("detectedDetail");
        return mav;
    }

    /**
     * 가품탐지된 제품의 크롤링 데이터 불러오기
     * @param id
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "가품탐지 된 제품의 크롤링 데이터", notes = "가품탐지 된 제품의 크롤링 데이터를 불러온다.")
    @GetMapping(value = "/detected/product/data")
    public ResponseEntity<Message> detectedProductData(
            @ApiParam(value = "제품 아이디", required = true) Long id) {

        JSONObject jsonData = detectedProductService.findData(id);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");
        message.setData(jsonData);

        return ResponseEntity.ok(message);
    }

}

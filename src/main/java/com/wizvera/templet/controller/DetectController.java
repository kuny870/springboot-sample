package com.wizvera.templet.controller;

import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.wizvera.templet.model.DetectedProduct;
import com.wizvera.templet.model.DetectingProduct;
import com.wizvera.templet.model.DetectingProductImage;
import com.wizvera.templet.model.response.Message;
import com.wizvera.templet.model.response.StatusEnum;
import com.wizvera.templet.repository.DetectedProductRepository;
import com.wizvera.templet.repository.DetectingProductRepository;
import com.wizvera.templet.service.DetectedProductService;
import com.wizvera.templet.service.DetectingProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@Api(tags = {"가품 탐지 정보를 제공하는 Controller"})
@RestController
@RequestMapping
@RequiredArgsConstructor
public class DetectController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final DetectingProductService detectingProductService;

    private final DetectedProductService detectedProductService;

    private final DetectingProductRepository detectingProductRepository;

    private final DetectedProductRepository detectedProductRepository;

    /**
     * 가품탐지할 제품 불러오기
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "가품탐지할 제품 전체 불러오기")
    @GetMapping(value = "/detecting/product/list")
    public ResponseEntity<Message> detectingProductList(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum
            , @RequestParam(value = "size", defaultValue = "10") Integer size) {

        PageRequest pageRequest = PageRequest.of(pageNum-1, size);
        Page<DetectingProduct> detectingProductList = detectingProductService.getDetectingProductList(pageRequest);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");
        message.setData(detectingProductList);

        return ResponseEntity.ok(message);
    }


    /**
     * 가품탐지할 제품 상세 불러오기
     * @param id
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "가품탐지할 제품 상세 불러오기")
    @GetMapping(value = "/detecting/product/detail")
    public ResponseEntity<Message> detectingProductDetail(Long id) {

        Optional<DetectingProduct> optionalDetectingProduct = detectingProductRepository.findById(id);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");
        message.setData(optionalDetectingProduct);

        return ResponseEntity.ok(message);
    }

    /**
     * 가품탐지할 제품 등록하기
     * @param detectingProduct
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "가품탐지할 제품 등록하기")
    @PostMapping(value = "/detecting/product/regist", consumes = {"multipart/form-data"})
    public ResponseEntity<Message> detectingProductRegist(
            DetectingProduct detectingProduct) throws IOException {

        detectingProductService.regist(detectingProduct);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");

        return ResponseEntity.ok(message);
    }

    /**
     * 가품탐지할 제품 수정하기
     * @param detectingProduct
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "가품탐지할 제품 수정하기")
    @PostMapping(value = "/detecting/product/update", consumes = {"multipart/form-data"})
    public ResponseEntity<Message> detectingProductUpdate(
            DetectingProduct detectingProduct) throws IOException {

        Optional<DetectingProduct> tempDp = detectingProductRepository.findById(detectingProduct.getId());
        detectingProduct.setCreatedDate(tempDp.get().getCreatedDate());

        detectingProductService.update(detectingProduct);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");

        return ResponseEntity.ok(message);
    }


    /**
     * 가품탐지할 제품 삭제하기
     * @param id
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "가품탐지할 제품 삭제하기")
    @PostMapping(value = "/detecting/product/delete")
    public ResponseEntity<Message> detectingProductDelete(Long id) {

        detectingProductService.delete(id);

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
    @ApiOperation(value = "가품탐지된 제품 전체 불러오기")
    @GetMapping(value = "/detected/product/list")
    public ResponseEntity<Message> detectedProductList(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum
            , @RequestParam(value = "size", defaultValue = "10") Integer size) {

        PageRequest pageRequest = PageRequest.of(pageNum-1, size);
        Page<DetectedProduct> detectedProductList = detectedProductService.getDetectedProductList(pageRequest);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");
        message.setData(detectedProductList);

        return ResponseEntity.ok(message);
    }


    /**
     * 가품탐지된 제품 상세 불러오기
     * @param id
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "가품탐지된 제품 상세 불러오기")
    @GetMapping(value = "/detected/product/detail")
    public ResponseEntity<Message> detectedProductDetail(Long id) {

        Optional<DetectedProduct> optionalDetectedProduct = detectedProductRepository.findById(id);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");
        message.setData(optionalDetectedProduct);

        return ResponseEntity.ok(message);
    }


    /**
     * 가품탐지된 제품 등록하기
     * @param detectedProduct
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "가품탐지된 제품 등록하기")
    @PostMapping(value = "/detected/product/regist")
    public ResponseEntity<Message> detectedProductRegist(
            DetectedProduct detectedProduct) {

        detectedProductService.regist(detectedProduct);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");

        return ResponseEntity.ok(message);
    }


    /**
     * 가품탐지된 제품 삭제하기
     * @param id
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "가품탐지된 제품 삭제하기")
    @PostMapping(value = "/detected/product/delete")
    public ResponseEntity<Message> detectedProductDelete(Long id) {

        detectedProductService.delete(id);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");

        return ResponseEntity.ok(message);
    }











    /**
     * 가품탐지할 제품 등록 페이지
     * @return
     */
    @ApiOperation(value = "가품탐지할 제품 등록")
    @GetMapping("/detecting-regist")
    public ModelAndView detectingRegist() {  // 가품탐지할 제품 등록
        ModelAndView mav = new ModelAndView();
        mav.setViewName("detectingRegist");
        return mav;
    }


    /**
     * 가품탐지 제품 등록
     * @return
     */
    @RequestMapping(value = "/detecting/product/regist2")
    public ModelAndView detectedProductRegist2(
            DetectingProduct detectingProduct
            , ModelAndView mav) throws IOException {

        detectingProductService.regist(detectingProduct);

        mav.setViewName("UserPage");
        return mav;
    }


    /**
     * 가품탐지할 제품 목록
     * @return
     */
    @RequestMapping(value = "detecting-list")
    public ModelAndView detectingList(
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
    @ApiOperation(value = "제품 삭제 시키기")
    @GetMapping("/detecting/remove")
    public ResponseEntity<Message> detectingRemove(String id) {

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
    @ApiOperation(value = "제품 복구 시키기")
    @GetMapping("/detecting/restore")
    public ResponseEntity<Message> detectingRestore(String id) {

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
    @ApiOperation(value = "가품탐지할 제품 상세 불러오기")
    @GetMapping(value = "/detecting/product/detail2")
    public ModelAndView productDetail(Long id, ModelAndView mav) {

        Optional<DetectingProduct> optionalDetectingProduct = detectingProductRepository.findById(id);
        DetectingProduct dp = optionalDetectingProduct.get();

        for(int i=0; i<dp.getDpiList().size(); i++) {
            dp.getDpiList().get(i).setProductImageString(Base64.getEncoder().encodeToString(dp.getDpiList().get(i).getProductImage()));
        }

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
    @ApiOperation(value = "가품탐지할 제품 수정하기")
    @PostMapping(value = "/detecting/product/update2", consumes = {"multipart/form-data"})
    public ResponseEntity<Message> detectingProductUpdate2(
            DetectingProduct detectingProduct
            , HttpServletResponse response) throws IOException {

        Optional<DetectingProduct> tempDp = detectingProductRepository.findById(detectingProduct.getId());
        detectingProduct.setCreatedDate(tempDp.get().getCreatedDate());

        detectingProductService.update(detectingProduct);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");

        response.sendRedirect("/detecting-list");
        return ResponseEntity.ok(message);
    }



    /**
     * 가품탐지된 제품 등록하기
     * @param detectedProduct
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "가품탐지된 제품 등록하기")
    @GetMapping(value = "/detected/product/regist2")
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
    @ApiOperation(value = "가품탐지된 제품 전체 불러오기")
    @GetMapping(value = "/detected/product/list2")
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
    @ApiOperation(value = "가품탐지된 제품 상세 불러오기")
    @GetMapping(value = "/detected/product/detail2")
    public ModelAndView detectedProductDetail2(Long id, ModelAndView mav) {

        Optional<DetectedProduct> optionalDetectedProduct = detectedProductRepository.findById(id);

        mav.addObject("dp", optionalDetectedProduct.get());
        mav.setViewName("detectedDetail");
        return mav;
    }

}

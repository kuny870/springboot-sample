package com.wizvera.templet.controller;

import com.wizvera.templet.model.DetectedProduct;
import com.wizvera.templet.model.DetectingProduct;
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

import java.io.IOException;
import java.util.Optional;

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
     * @param file
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


}

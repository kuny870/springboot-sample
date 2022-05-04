package com.wizvera.templet.controller.api;

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
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@Api(tags = { "201. 가품탐지 된 제품 정보" })
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping(value = "/api/detected")
public class DetectedApiController {

    private final DetectedProductService    detectedProductService;
    private final DetectedProductRepository detectedProductRepository;

    @ApiOperation(value = "가품탐지 된 제품 전체 목록 불러오기", notes = "가품탐지 된 제품의 전체 목록을 불러온다.")
    @GetMapping(value = "/product/list")
    public ResponseEntity<Message> detectedProductList(
            @ApiParam(value = "페이지번호", required = false) @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @ApiParam(value = "폐이지당 데이터 개수", required = false) @RequestParam(value = "size", defaultValue = "10") Integer size) {

        PageRequest pageRequest = PageRequest.of(pageNum-1, size);
        Page<DetectedProduct> detectedProductList = detectedProductService.getDetectedProductList(pageRequest);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");
        message.setData(detectedProductList);

        return ResponseEntity.ok(message);
    }

    @ApiOperation(value = "가품탐지 된 제품 상세 불러오기", notes = "가품탐지 된 제품의 상세 정보를 불러온다.")
    @GetMapping(value = "/product/detail")
    public ResponseEntity<Message> detectedProductDetail(
            @ApiParam(value = "제품 아이디", required = true) Long id) {

        Optional<DetectedProduct> optionalDetectedProduct = detectedProductRepository.findById(id);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");
        message.setData(optionalDetectedProduct);

        return ResponseEntity.ok(message);
    }

    @ApiOperation(value = "가품탐지 된 제품 등록", notes = "가품탐지 된 제품을 등록한다.")
    @PostMapping(value = "/product/regist")
    public ResponseEntity<Message> detectedProductRegist(
            @ApiParam(value = "제품 정보", required = true) DetectedProduct detectedProduct) {

        detectedProductService.regist(detectedProduct);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");

        return ResponseEntity.ok(message);
    }

    @ApiOperation(value = "가품탐지 된 제품 삭제", notes = "가품탐지 된 제품을 삭제한다.")
    @PostMapping(value = "/product/delete")
    public ResponseEntity<Message> detectedProductDelete(
            @ApiParam(value = "제품 아이디", required = true) Long id) {

        detectedProductService.delete(id);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");

        return ResponseEntity.ok(message);
    }

    @ApiOperation(value = "가품탐지 된 제품의 크롤링 데이터", notes = "가품탐지 된 제품의 크롤링 데이터를 불러온다.")
    @GetMapping(value = "/product/data")
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

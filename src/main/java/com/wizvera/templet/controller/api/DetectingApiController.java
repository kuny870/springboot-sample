package com.wizvera.templet.controller.api;

import com.wizvera.templet.model.DetectingProduct;
import com.wizvera.templet.model.response.Message;
import com.wizvera.templet.model.response.StatusEnum;
import com.wizvera.templet.repository.DetectingProductRepository;
import com.wizvera.templet.service.DetectingProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@Api(tags = { "200. 가품 탐지 할 제품 정보" })
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping(value = "/api/detecting")
public class DetectingApiController {

    private final DetectingProductService    detectingProductService;
    private final DetectingProductRepository detectingProductRepository;

    @ApiOperation(value = "가품탐지 할 제품 목록 불러오기", notes = "가품탐지 할 제품 전체 목록을 불러온다.")
    @GetMapping(value = "/product/list")
    public ResponseEntity<Message> detectingProductList(
            @ApiParam(value = "페이지번호", required = false) @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @ApiParam(value = "폐이지당 데이터 개수", required = false) @RequestParam(value = "size", defaultValue = "10") Integer size) {

        PageRequest pageRequest = PageRequest.of(pageNum-1, size);
        Page<DetectingProduct> detectingProductList = detectingProductService.getDetectingProductList(pageRequest);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");
        message.setData(detectingProductList);

        return ResponseEntity.ok(message);
    }

    @ApiOperation(value = "가품탐지 할 제품 등록", notes = "가품탐지 할 제품을 등록한다.")
    @PostMapping(value = "/product/regist", consumes = {"multipart/form-data"})
    public ResponseEntity<Message> detectingProductRegist(
            @ApiParam(value = "제품 정보", required = true) DetectingProduct detectingProduct) throws IOException {

        detectingProductService.regist(detectingProduct);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");

        return ResponseEntity.ok(message);
    }

    @ApiOperation(value = "가품탐지 할 제품 상세 불러오기", notes = "가품탐지 할 제품의 상세 정보를 불러온다.")
    @GetMapping(value = "/product/detail")
    public ResponseEntity<Message> detectingProductDetail(
            @ApiParam(value = "제품 아이디", required = true) Long id) {

        Optional<DetectingProduct> optionalDetectingProduct = detectingProductRepository.findById(id);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");
        message.setData(optionalDetectingProduct);

        return ResponseEntity.ok(message);
    }

    @ApiOperation(value = "가품탐지 할 제품 수정", notes = "가품탐지 할 제품을 수정한다.")
    @PostMapping(value = "/product/update", consumes = {"multipart/form-data"})
    public ResponseEntity<Message> detectingProductUpdate(
            @ApiParam(value = "제품 정보", required = true) DetectingProduct detectingProduct) throws IOException {

        Optional<DetectingProduct> tempDp = detectingProductRepository.findById(detectingProduct.getId());
        detectingProduct.setCreatedDate(tempDp.get().getCreatedDate());

        detectingProductService.update(detectingProduct);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");

        return ResponseEntity.ok(message);
    }

    @ApiOperation(value = "가품탐지 할 제품 삭제", notes = "가품탐지 할 제품을 삭제한다.")
    @PostMapping(value = "/product/delete")
    public ResponseEntity<Message> detectingProductDelete(
            @ApiParam(value = "제품 아이디", required = true) Long id) {

        detectingProductService.delete(id);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");

        return ResponseEntity.ok(message);
    }

    @ApiOperation(value = "가품탐지 할 제품 복구", notes = "삭제된 가품탐지 할 제품을 복구한다.")
    @GetMapping("/product/restore")
    public ResponseEntity<Message> detectingProductRestore(
            @ApiParam(value = "제품 아이디", required = true) String id) {

        detectingProductRepository.updateDetectingRestore(id);

        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setMessage("성공 코드");

        return ResponseEntity.ok(message);
    }


}

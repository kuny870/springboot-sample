package com.wizvera.templet.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@ApiModel(value = "가품탐지된 제품 이력")
public class DetectedProductHistory extends TimeEntity {

    @ApiModelProperty(value = "pk")
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "가품탐지된 제품 pk")
    @Column(name = "detected_product_id")
    private Long detectedProductId;

    @ApiModelProperty(value = "페이지 아이디")
    @Column(name = "page_id")
    private String pageId;

    @ApiModelProperty(value = "페이지 주소")
    @Column(name = "page_address")
    private String pageAddress;

    @ApiModelProperty(value = "페이지 메인 이미지")
    @Column(name = "page_main_image")
    private String pageMainImage;

    @ApiModelProperty(value = "페이지 사이트명")
    @Column(name = "page_site")
    private String pageSite;

    @ApiModelProperty(value = "판매자")
    @Column(name = "seller")
    private String seller;

    @ApiModelProperty(value = "원래 가격")
    @Column(name = "origin_price")
    private int originPrice;

    @ApiModelProperty(value = "판매 가격")
    @Column(name = "price")
    private int price;

    @ApiModelProperty(value = "평점")
    @Column(name = "average_score")
    private String averageScore;

    @ApiModelProperty(value = "리뷰개수")
    @Column(name = "review_count")
    private String reviewCount;

    @ApiModelProperty(value = "가품 퍼센트")
    @Column(name = "imitation_percentage")
    private String imitationPercentage;

}
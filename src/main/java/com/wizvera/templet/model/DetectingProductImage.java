package com.wizvera.templet.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@ApiModel(value = "가품탐지할 제품 이미지")
public class DetectingProductImage {

    @ApiModelProperty(value = "pk")
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "가품탐지할 제품 pk")
    @Column(name = "detecting_product_id")
    private Long detectingProductId;

    @ApiModelProperty(value = "타입")
    @Column(name = "type")
    private String type;

    @ApiModelProperty(value = "제품 이미지")
    @Lob
    @Column(name = "product_image", columnDefinition = "LONGBLOB")
    private byte[] productImage;

    @Transient
    private String productImageString;

}
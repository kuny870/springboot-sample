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
@ApiModel(value = "가품탐지할 검색 키워드")
public class DetectingSearchKeyword {

    @ApiModelProperty(value = "pk")
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "가품탐지할 제품 pk")
    @Column(name = "detecting_product_id")
    private Long detectingProductId;

    @ApiModelProperty(value = "검색 키워드")
    @Column(name = "search_keyword")
    private String searchKeyword;

}
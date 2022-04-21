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
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@ApiModel(value = "가품탐지할 제품")
public class DetectingProduct extends TimeEntity {

    @ApiModelProperty(value = "pk")
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "사용자 pk")
    @Column(name = "reg_user")
    private Long regUser;

    @ApiModelProperty(value = "제품명")
    @Column(name = "product_name")
    private String productName;

    @ApiModelProperty(value = "검색 사이트")
    @Column(name = "search_site")
    private String searchSite;

    @ApiModelProperty(value = "삭제여부", example = "N")
    @Column(name = "del_yn", columnDefinition = "CHAR(1) NOT NULL DEFAULT 'N'")
    private String delYn;

    @ApiModelProperty(value = "제품탐지할 제품 이미지 리스트")
    @OneToMany
    @JoinColumn(name = "detecting_product_id")
    private List<DetectingProductImage> dpiList = new ArrayList<>();

    @ApiModelProperty(value = "제품탐지할 검색 키워드")
    @OneToMany
    @JoinColumn(name = "detecting_product_id")
    private List<DetectingSearchKeyword> dskList = new ArrayList<>();

    @ApiModelProperty(value = "제품탐지할 검색 키워드 파라미터")
    @Transient
    private ArrayList searchKeyword;

    @Transient
    private List<MultipartFile> file;

    @Builder
    public DetectingProduct(String productName, String searchSite) {
        this.productName = productName;
        this.searchSite = searchSite;
        this.delYn = "N";
    }

}
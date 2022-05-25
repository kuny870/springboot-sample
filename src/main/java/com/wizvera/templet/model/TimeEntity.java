package com.wizvera.templet.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public abstract class TimeEntity {

    @ApiModelProperty(value = "가입날짜")
    @Column(name = "created_date")
    @CreatedDate
    private LocalDateTime createdDate;

    @ApiModelProperty(value = "수정날짜")
    @Column(name = "modified_date")
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    /* 해당 엔티티를 저장하기 이전에 실행 */
    @PrePersist
    public void onPrePersist(){
        this.createdDate = LocalDateTime.now();
        this.modifiedDate = this.createdDate;
    }

    /* 해당 엔티티를 업데이트 하기 이전에 실행*/
    @PreUpdate
    public void onPreUpdate(){
        this.modifiedDate = LocalDateTime.now();
    }

}

package com.wizvera.templet.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user_status")
public class UserStatus {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private int code;

    @Column(name = "description")
    private String description;

    public UserStatus() {
        this.code = 0;
    }

    public UserStatus(int code) {
        this.code = code;
        this.description = parseDescription(code);
    }

    public boolean isDisplayed() {
        return code == 0;
    }

    private String parseDescription(int code) {
        String result = "";
        if(code == 0) {
            result = "일반";
        }else if(code == 1){
            result = "계정만료";
        }
        return result;
    }
}

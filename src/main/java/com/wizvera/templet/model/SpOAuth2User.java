package com.wizvera.templet.model;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static java.lang.String.format;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
@Table(name="sp_oauth2_user")
@DynamicInsert
@DynamicUpdate
public class SpOAuth2User {

    @Id
    private String oauth2UserId;    // google-{id}, naver-{id}

    private Long userId;    // User

    private String userUserId;

    private String name;
    private String email;
    private String phoneNumber;

    @CreatedDate
    private String created;
    private Provider provider;

    /* 해당 엔티티를 저장하기 이전에 실행 */
    @PrePersist
    public void onPrePersist(){
        this.created = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
    }

    public static enum Provider {
        google {
            public SpOAuth2User convert(OAuth2User user){
                return SpOAuth2User.builder()
                        .oauth2UserId(format("%s_%s", name(), user.getAttribute("sub")))
                        .provider(google)
                        .userUserId(user.getAttribute("userId"))
                        .email(user.getAttribute("email"))
                        .name(user.getAttribute("name"))
                        .phoneNumber(user.getAttribute("phoneNumber"))
                        .build();
            }
        },
        naver {
            public SpOAuth2User convert(OAuth2User user){
                Map<String, Object> resp = user.getAttribute("response");
                return SpOAuth2User.builder()
                        .oauth2UserId(format("%s_%s", name(), resp.get("id")))
                        .provider(naver)
                        .userUserId(""+resp.get("userId"))
                        .email(""+resp.get("email"))
                        .name(""+resp.get("name"))
                        .phoneNumber(""+resp.get("phoneNumber"))
                        .build();
            }
        };

        public abstract SpOAuth2User convert(OAuth2User userInfo);
    }

}
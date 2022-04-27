package com.wizvera.templet.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@ApiModel(value = "사용자")
public class User extends TimeEntity implements UserDetails {

    @ApiModelProperty(value = "pk")
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "사용자의 아이디", example = "admin", required = true)
    @Column(name = "user_id", unique = true, columnDefinition = "VARCHAR(45) NOT NULL")
    private String userId;

    @ApiModelProperty(value = "비밀번호", example = "1234")
    @Column(name = "password")
    private String password;

    @ApiModelProperty(value = "이메일", example = "admin@naver.com")
    @Column(name = "email")
    private String email;

    @ApiModelProperty(value = "이름", example = "홍길동")
    @Column(name = "name")
    private String name;

    @ApiModelProperty(value = "연락처", example = "010-1234-1234")
    @Column(name = "phone_number")
    private String phoneNumber;

    @ApiModelProperty(value = "회사명", example = "(주)위즈베라")
    @Column(name = "company_name")
    private String companyName;

    @ApiModelProperty(value = "사업자번호", example = "220-81-62517")
    @Column(name = "business_no")
    private String businessNo;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    public User(User details) {
        super();
    }

    // 사용자의 권한을 콜렉션 형태로 반환
    // 단, 클래스 자료형은 GrantedAuthority를 구현해야함
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @ApiModelProperty(value = "승인여부", example = "Y")
    @Column(name = "approval", columnDefinition = "CHAR(1) NOT NULL DEFAULT 'N'")
    private String approval;

    @ApiModelProperty(value = "삭제여부", example = "N")
    @Column(name = "del_yn", columnDefinition = "CHAR(1) NOT NULL DEFAULT 'N'")
    private String delYn;


    @Builder
    public User(String userId, String email, String password, String name, String phoneNumber, List<String> roles) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.delYn = "N";
        this.roles = roles;
    }

    // 사용자의 id를 반환 (unique한 값)
    @Override
    public String getUsername() {
        return userId;
    }

    // 사용자의 password를 반환
    @Override
    public String getPassword() {
        return password;
    }

    // 계정 만료 여부 반환
    @Override
    public boolean isAccountNonExpired() {
        // 만료되었는지 확인하는 로직
        return true; // true -> 만료되지 않았음
    }

    // 계정 잠금 여부 반환
    @Override
    public boolean isAccountNonLocked() {
        // 계정 잠금되었는지 확인하는 로직
        return true; // true -> 잠금되지 않았음
    }

    // 패스워드의 만료 여부 반환
    @Override
    public boolean isCredentialsNonExpired() {
        // 패스워드가 만료되었는지 확인하는 로직
        return true; // true -> 만료되지 않았음
    }

    // 계정 사용 가능 여부 반환
    @Override
    public boolean isEnabled() {
        // 계정이 사용 가능한지 확인하는 로직
        return true; // true -> 사용 가능
    }

}
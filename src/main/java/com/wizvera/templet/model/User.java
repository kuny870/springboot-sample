package com.wizvera.templet.model;

import com.wizvera.templet.model.converter.UserStatusConverter;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Data
@Entity
@DynamicInsert
@DynamicUpdate
public class User extends TimeEntity implements UserDetails {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "auth", columnDefinition = "varchar(255) NOT NULL DEFAULT 'ROLE_USER'")
    private String auth;

    @Column(name = "status", columnDefinition = "int DEFAULT 0")
    @Convert(converter = UserStatusConverter.class)
    private UserStatus status;    // 유저 상태

    @Column(name = "enabled")
    private boolean enabled;

//    public static enum State {
//        NORMAL, // 일반 사용자
//        EXPIRATION // 계정 만료 사용자
//    }

    @Column(name = "del_yn", columnDefinition = "CHAR(1) NOT NULL DEFAULT 'N'")
    private String delYn;


    @Builder
    public User(String email, String password, String name, String auth, String delYn, UserStatus status) {
        this.email = email;
        this.password = password;
        this.name = name;
        if(auth == null) {
            this.auth = "ROLE_USER";
        }else {
            this.auth = auth;
        }
        this.delYn = "N";
        this.status = status;
    }

    // 사용자의 권한을 콜렉션 형태로 반환
    // 단, 클래스 자료형은 GrantedAuthority를 구현해야함
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> roles = new HashSet<>();
        for (String role : auth.split(",")) {
            roles.add(new SimpleGrantedAuthority(role));
        }
        return roles;
    }

    // 사용자의 id를 반환 (unique한 값)
    @Override
    public String getUsername() {
        return name;
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
package com.wizvera.templet.model;

import lombok.*;

import javax.persistence.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NonNull
    @Column(name = "login_id")
    private String loginId;

    @NonNull
    @Column(name = "login_pw")
    private String loginPw;

    @NonNull
    @Column(name = "name")
    private String name;

    @Column(name = "del_yn")
    private String delYn;
}

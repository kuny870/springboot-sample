package com.wizvera.templet.config.JWT;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginForm {

    private String username;
    private String password;
    private String refreshToken;

}

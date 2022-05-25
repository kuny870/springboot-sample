package com.wizvera.templet.config.JWT;

import com.wizvera.templet.model.User;
import com.wizvera.templet.repository.UserRepository;
import com.wizvera.templet.service.UserService;
import com.wizvera.templet.util.CryptoUtils;
import com.wizvera.templet.util.StringUtil;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * JWT 토큰을 생성하고, 토큰을 검증하는 클래스.
 *
 * @author jeehoon.song
 * @since 2022. 3. 17.
 */
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private String secretKey = "secret";

    private long tokenValidTime = 1000L * 60 * 60;  // 60분

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private final MessageSource messageSource;

    // JWT 토큰 생성
    public String createToken(String userPk, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(userPk); // JWT payload 에 저장되는 정보단위
        claims.put("roles", roles); // 정보는 key / value 쌍으로 저장된다.
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
//                .setExpiration(new Date(now.getTime() + tokenValidTime)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        String userPk = this.getUserPk(token);
        UserDetails userDetails = userService.loadUserByUsername(this.getUserPk(token));

        // 저장된 token과 전달받은 token이 다르면 다시 로그인
//        Optional<User> OptionalUser = userRepository.findByUserId(userPk);
//        User user = OptionalUser.get();
//
//        if ( StringUtil.isBlank(user.getToken()) ) {
//            int errCode = Integer
//                    .valueOf(messageSource.getMessage("loginRetry.code", null, LocaleContextHolder.getLocale()));
//            String errMsg = messageSource.getMessage("loginRetry.msg", null, LocaleContextHolder.getLocale());
//            throw new JwtAuthException(errCode, errMsg);
//        }
//
//        try {
//            if ( !CryptoUtils.genHexSHA256(token, 1).equals(user.getToken()) ) {
//                int errCode = Integer
//                        .valueOf(messageSource.getMessage("tokenInvalid.code", null, LocaleContextHolder.getLocale()));
//                String errMsg = messageSource.getMessage("tokenInvalid.msg", null, LocaleContextHolder.getLocale());
//                throw new JwtAuthException(errCode, errMsg);
//            }
//        }
//        catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // Request의 Header에서 token 값을 가져옵니다. "X-AUTH-TOKEN" : "TOKEN값'
    public String resolveToken(HttpServletRequest request) {
        String token = null;
        Cookie cookie = WebUtils.getCookie(request, "X-AUTH-TOKEN");
        if(cookie != null) token = cookie.getValue();
        return token;
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
//			validateTokenIsNotForALoggedOutDevice(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        }
        catch (ExpiredJwtException e) {
            int errCode = Integer
                    .valueOf(messageSource.getMessage("tokenExpired.code", null, LocaleContextHolder.getLocale()));
            String errMsg = messageSource.getMessage("tokenExpired.msg", null, LocaleContextHolder.getLocale());
            throw new JwtAuthException(errCode, errMsg);
            // return false;
        }
        catch (SignatureException e) {
            int errCode = Integer.valueOf(messageSource
                    .getMessage("tokenSignatureError.code", null, LocaleContextHolder.getLocale()));
            String errMsg = messageSource.getMessage("tokenSignatureError.msg", null, LocaleContextHolder.getLocale());
            throw new JwtAuthException(errCode, errMsg);
            // return false;
        }
        catch (IllegalArgumentException e) {
            int errCode = Integer
                    .valueOf(messageSource.getMessage("tokenIsEmpty.code", null, LocaleContextHolder.getLocale()));
            String errMsg = messageSource.getMessage("tokenIsEmpty.msg", null, LocaleContextHolder.getLocale());
            throw new JwtAuthException(errCode, errMsg);
            // return false;
        }
        catch (MalformedJwtException e) {
            int errCode = Integer
                    .valueOf(messageSource.getMessage("tokenIsMalformed.code", null, LocaleContextHolder.getLocale()));
            String errMsg = messageSource.getMessage("tokenIsMalformed.msg", null, LocaleContextHolder.getLocale());
            throw new JwtAuthException(errCode, errMsg);
            // return false;
        }

        catch (Exception e) {
            e.printStackTrace();
            int errCode = Integer
                    .valueOf(messageSource.getMessage("tokenVerifyError.code", null, LocaleContextHolder.getLocale()));
            String errMsg = messageSource.getMessage("tokenVerifyError.msg", null, LocaleContextHolder.getLocale());
            throw new JwtAuthException(errCode, errMsg);
            // return false;
        }
        // return true;
    }
}
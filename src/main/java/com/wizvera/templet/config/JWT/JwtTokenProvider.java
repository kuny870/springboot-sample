//package com.wizvera.templet.config.JWT;
//
//import com.wizvera.templet.model.User;
//import com.wizvera.templet.repository.UserRepository;
//import com.wizvera.templet.util.CryptoUtils;
//import com.wizvera.templet.util.StringUtil;
//import io.jsonwebtoken.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.MessageSource;
//import org.springframework.context.i18n.LocaleContextHolder;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import javax.servlet.http.HttpServletRequest;
//import java.io.UnsupportedEncodingException;
//import java.security.NoSuchAlgorithmException;
//import java.util.Base64;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//
///**
// * JWT 토큰을 생성하고, 토큰을 검증하는 클래스.
// *
// * @author jeehoon.song
// * @since 2022. 3. 17.
// */
//@RequiredArgsConstructor
//@Component
//public class JwtTokenProvider {
//
//    @Value("${spring.jwt.secret}")
//    private String	secretKey;
//    @Value("${spring.jwt.valid-period:30}")
//    private Integer	secretValidPeriod;
//
//    private long tokenValidMilisecond;
//
//    private final UserDetailsService userDetailsService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private final MessageSource messageSource;
//
//    @PostConstruct
//    protected void init() {
//        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
//        tokenValidMilisecond = 1000L * 60 * secretValidPeriod;
//    }
//
//    // Jwt 토큰 생성
//    public String createToken(	String userPk,
//                                  List<String> roles) {
//        Claims claims = Jwts.claims().setSubject(userPk);
//        claims.put("roles", roles);
//        Date now = new Date();
//        return Jwts.builder().setClaims(claims) // 데이터
//                .setIssuedAt(now) // 토큰 발행일자
//                .setExpiration(new Date(now.getTime() + tokenValidMilisecond)) // set Expire Time
//                .signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘, secret값 세팅
//                .compact();
//        // return "1111";
//    }
//
//    /**
//     * 전달된 JWT 토큰이 발급된 사용자의 인증정보({@link Authentication})를 반환한다.
//     * <p>
//     * 전달된 토큰에서 사용자 정보를 추출하고, 해당 사용자에게 부여된 인증정보를 추출하여 반환한다.
//     *
//     * @param token
//     * @return
//     */
//    public Authentication getAuthentication(String token) {
//        String userPk = this.getUserPk(token);
//        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
//
//        // 저장된 token과 전달받은 token이 다르면 다시 로그인
//        Optional<User> optionalUser = userRepository.findByUserId(userPk);
//        User user = optionalUser.get();
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
//
//        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//    }
//
//    /**
//     * 전달된 JWT 토큰을 파싱하여 사용자의 식별정보를 반환한다.
//     *
//     * @param token
//     * @return
//     */
//    public String getUserPk(String token) {
//        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
//        // return "1111";
//    }
//
//    /**
//     * HTTP Request의 Header에서 token을 추출하여 반환한다.
//     * <p>
//     * Header의 key값 중 'X-AUTH-TOKEN'에 해당하는 값을 추출한다.
//     *
//     * @param req
//     * @return
//     */
//    public String resolveToken(HttpServletRequest req) {
//        return req.getHeader("X-AUTH-TOKEN");
//    }
//
//    /**
//     * 전달된 JWT 토큰을 파싱하여 토큰 자체의 유효성과 만료 여부를 확인하여 통합 유효 여부를 반환한다.
//     *
//     * @param jwtToken
//     * @return
//     */
//    public boolean validateToken(String jwtToken) {
//        try {
//            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
////			validateTokenIsNotForALoggedOutDevice(jwtToken);
//            return !claims.getBody().getExpiration().before(new Date());
//        }
//        catch (ExpiredJwtException e) {
//            int errCode = Integer
//                    .valueOf(messageSource.getMessage("tokenExpired.code", null, LocaleContextHolder.getLocale()));
//            String errMsg = messageSource.getMessage("tokenExpired.msg", null, LocaleContextHolder.getLocale());
//            throw new JwtAuthException(errCode, errMsg);
//            // return false;
//        }
//        catch (SignatureException e) {
//            int errCode = Integer.valueOf(messageSource
//                    .getMessage("tokenSignatureError.code", null, LocaleContextHolder.getLocale()));
//            String errMsg = messageSource.getMessage("tokenSignatureError.msg", null, LocaleContextHolder.getLocale());
//            throw new JwtAuthException(errCode, errMsg);
//            // return false;
//        }
//        catch (IllegalArgumentException e) {
//            int errCode = Integer
//                    .valueOf(messageSource.getMessage("tokenIsEmpty.code", null, LocaleContextHolder.getLocale()));
//            String errMsg = messageSource.getMessage("tokenIsEmpty.msg", null, LocaleContextHolder.getLocale());
//            throw new JwtAuthException(errCode, errMsg);
//            // return false;
//        }
//        catch (MalformedJwtException e) {
//            int errCode = Integer
//                    .valueOf(messageSource.getMessage("tokenIsMalformed.code", null, LocaleContextHolder.getLocale()));
//            String errMsg = messageSource.getMessage("tokenIsMalformed.msg", null, LocaleContextHolder.getLocale());
//            throw new JwtAuthException(errCode, errMsg);
//            // return false;
//        }
//
//        catch (Exception e) {
//            e.printStackTrace();
//            int errCode = Integer
//                    .valueOf(messageSource.getMessage("tokenVerifyError.code", null, LocaleContextHolder.getLocale()));
//            String errMsg = messageSource.getMessage("tokenVerifyError.msg", null, LocaleContextHolder.getLocale());
//            throw new JwtAuthException(errCode, errMsg);
//            // return false;
//        }
//        // return true;
//    }
//
//    public Date getTokenExpiryFromJWT(String jwtToken) {
//        Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
//        return claims.getBody().getExpiration();
//    }
//}
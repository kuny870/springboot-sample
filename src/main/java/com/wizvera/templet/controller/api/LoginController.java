//package com.wizvera.templet.controller.api;
//
//import com.wizvera.templet.config.JWT.JwtTokenProvider;
//import com.wizvera.templet.logging.LogMdcKey;
//import com.wizvera.templet.model.User;
//import com.wizvera.templet.model.request.LoginReq;
//import com.wizvera.templet.model.response.SingleResult;
//import com.wizvera.templet.repository.UserRepository;
//import com.wizvera.templet.service.ResponseService;
//import com.wizvera.templet.util.CryptoUtils;
//import com.wizvera.templet.util.HttpUtils;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.MDC;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.UnsupportedEncodingException;
//import java.security.NoSuchAlgorithmException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Api(tags = { "100. 로그인" })
//@RequiredArgsConstructor
//@RestController
//@Slf4j
//@RequestMapping(value = "/auth")
//public class LoginController implements LogMdcKey {
//
//    private final UserRepository userRepository;
//    private final JwtTokenProvider jwtTokenProvider;
//
//    private final ResponseService responseService;
//    private final PasswordEncoder passwordEncoder;
//
//    @ApiOperation(value = "로그인-JSON", notes = "관리자 로그인을 한다.")
//    @PostMapping(value = "/login")
//    @Transactional
//    public SingleResult<String> login(HttpServletRequest request,
//                                      @ApiParam(value = "json 형태", required = true) @RequestBody LoginReq loginReq) {
//       log.trace("[@TRACE@][called-login()]");
//
//        MDC.put(MDC_KEY_RESULT, MDC_VALUE_RESULT_NONE);
//        MDC.put(MDC_KEY_CATEGORY, MDC_CATEGORY_LOGIN);
//        MDC.put(MDC_KEY_ADMIN_ID, loginReq.getId());
//
//        MDC.put(MDC_KEY_AUDITEE_ID, "");
//        MDC.put(MDC_KEY_ADMIN_IP, HttpUtils.getClientIpAddress(request));
//
//        Optional<User> optionalUser = userRepository.findByUserId(loginReq.getId());
//        User user = optionalUser.get();
//        String password = loginReq.getPassword();
//
//        if ( user == null ) {
//            return responseService
//                    .getFailSingleResult(-1234, "there is no admin corresponding to the id(" + loginReq.getId() + ")");
//        }
//
//        MDC.put(MDC_KEY_ADMIN_TYPE, user.getUserGroup().getAccessRole());
//
////        if ( !passwordEncoder.matches(loginReq.getId() + password + "wvps", user.getPassword()) ) {
////            log.info(DB_AUDIT_PREFIX);
////            log.info(DB_LOGGING_PREFIX + "err[{}]", "password is not matched.");
////            throw new CEmailSigninFailedException();
////        }
//
//        List<String> roleList = new ArrayList<>();
//        if ( user.getUserGroup() == null ) {
//            log.info(DB_AUDIT_PREFIX);
//            log.info(DB_LOGGING_PREFIX + "err[{}]", "there is no admin-group to which you belong.");
//            responseService.getFailSingleResult(-4434, "there is no admin-group to which you belong.");
//        }
//        roleList.add(user.getUserGroup().getAccessRole());
//
//        String token = jwtTokenProvider.createToken(String.valueOf(user.getUserId()), roleList);
//        String hToken = null;
//        try {
//            hToken = CryptoUtils.genHexSHA256(token, 1);
//        }
//        catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
//            String errMsg = "error has occurred while generating the hashed-token.";
//            log.info(DB_AUDIT_PREFIX);
//            log.info(DB_LOGGING_PREFIX + "err[{}]", errMsg);
//
//            return responseService.getFailSingleResult(-2342, errMsg);
//        }
//        user.setToken(hToken);
//
//        log.info(DB_AUDIT_PREFIX);
//        log.info(DB_LOGGING_PREFIX + "{} logged in.", loginReq.getId());
//
//        return responseService.getSingleResult(token);
//    }
//
//
//}

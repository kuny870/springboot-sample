//package com.wizvera.templet;
//
//import com.wizvera.templet.config.JWT.UserLoginForm;
//import com.wizvera.templet.model.User;
//import com.wizvera.templet.repository.UserRepository;
//import com.wizvera.templet.service.UserService;
//import javassist.bytecode.DuplicateMemberException;
//import org.junit.Test;
//import org.junit.jupiter.api.DisplayName;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.client.RestTemplate;
//
//public class JWTRequestTest extends WebIntegrationTest{
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private UserService userService;
//
//    void before() throws DuplicateMemberException {
//        userRepository.deleteAll();
//        Long user = userService.save(User.builder()
//                .username("user1")
//                .password("1111")
//                .build());
//    }
//
//    @DisplayName("1. hello 메시지를 받아온다")
//    @Test
//    public void test_1() {
//
//        RestTemplate client = new RestTemplate();
//
//        HttpEntity<UserLoginForm> body = new HttpEntity<>(
//                UserLoginForm.builder().username("user1").password("1111").build()
//        );
//
//
//        ResponseEntity<String> resp1 =  client.exchange(uri("/login"), HttpMethod.PATCH, body, String.class);
//        System.out.println(resp1.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0));
//        System.out.println(resp1.getBody());
//    }
//
//}

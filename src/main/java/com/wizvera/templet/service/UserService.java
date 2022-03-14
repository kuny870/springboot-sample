package com.wizvera.templet.service;

import com.wizvera.templet.model.SpOAuth2User;
import com.wizvera.templet.model.User;
import com.wizvera.templet.repository.SpOAuth2UserRepository;
import com.wizvera.templet.repository.UserRepository;
import javassist.bytecode.DuplicateMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SpOAuth2UserRepository oAuth2UserRepository;

    /**
     * 사용자 전체 불러오기
     * @return
     */
    @Secured({"ROLE_ADMIN", "ROLE_RUN_AS_ADMIN"})
//    @PostFilter("filterObject.state == T(com.wizvera.templet.model.User.State).NORMAL")
    @PostFilter("filterObject.delYn == 'N'")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    /**
     * 사용자 한명 불러오기
     * @param email
     * @return
     */
    public User getUser(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()) {
            return user.get();
        }

        throw new EntityNotFoundException(
                "Cant find ant user under given ID"
        );
    }

    /**
     * 회원정보 저장
     *
     * @param user 회원정보가 들어있는 DTO
     * @return 저장되는 회원의 PK
     */
    public Long save(User user) throws DuplicateMemberException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));

        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());

        if(optionalUser.isPresent()){
            return 0L;
        }

        return userRepository.save(User.builder()
            .email(user.getEmail())
            .auth(user.getAuth())
            .password(user.getPassword()).build()).getId();
    }

    /**
     * 회원정보 수정
     * @param user
     * @return
     */
    public User updateUser(User user) {
        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());

        if(!optionalUser.isPresent()) {
            throw new EntityNotFoundException(
                    "User not present in the database"
            );
        }

        User getUser = optionalUser.get();

        // 회원 본인이 아니면 exception 발생
        if(getUser.getId() != ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()) {
            // 본인이 아니여도 관리자는 수정 가능
            if(!((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAuthorities().contains("ROLE_ADMIN")){
                throw new AccessDeniedException("access denied");
            }
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        getUser.setPassword(encoder.encode(user.getPassword()));

        return userRepository.save(getUser);
    }

    public User removeUser(User user) {
        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
        User getUser = optionalUser.get();
        getUser.setDelYn("Y");
        return userRepository.save(getUser);
    }


    /**
     * Spring Security 필수 메소드 구현
     *
     * @param email 이메일
     * @return UserDetails
     * @throws UsernameNotFoundException 유저가 없을 때 예외 발생
     */
    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException((email)));
    }

    // OAuth2 테스트
    public User load(SpOAuth2User oAuth2User) {
        SpOAuth2User dbUser = oAuth2UserRepository.findById(oAuth2User.getOauth2UserId())
                .orElseGet(()->{
                    User user = new User();
                    user.setEmail(oAuth2User.getEmail());
                    user.setName(oAuth2User.getName());
                    user.setEnabled(true);
                    userRepository.save(user);

                    oAuth2User.setUserId(user.getId());
                    return oAuth2UserRepository.save(oAuth2User);
                });
        return userRepository.findById(dbUser.getUserId()).get();
    }

}


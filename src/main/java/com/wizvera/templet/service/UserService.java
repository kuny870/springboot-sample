package com.wizvera.templet.service;

import com.wizvera.templet.model.User;
import com.wizvera.templet.repository.UserRepository;
import javassist.bytecode.DuplicateMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
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

    private final UserRepository userRepository;

    /**
     * 사용자 전체 불러오기
     * @return
     */
    @Secured({"ROLE_ADMIN", "ROLE_RUN_AS_ADMIN"})
    @PostFilter("filterObject.state == T(com.wizvera.templet.model.User.State).NORMAL")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    /**
     * 사용자 한명 불러오기
     * @param username
     * @return
     */
    public User getUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
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
     * @param infoDto 회원정보가 들어있는 DTO
     * @return 저장되는 회원의 PK
     */
    public Long save(User infoDto) throws DuplicateMemberException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        infoDto.setPassword(encoder.encode(infoDto.getPassword()));

        Optional<User> optionalUser = userRepository.findByUsername(infoDto.getUsername());

        if(optionalUser.isPresent()){
            return 0L;
        }

        return userRepository.save(User.builder()
            .username(infoDto.getUsername())
            .auth(infoDto.getAuth())
            .password(infoDto.getPassword()).build()).getId();
    }

    /**
     * 회원정보 수정
     * @param user
     * @return
     */
    public User updateUser(User user) {
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
        if(!optionalUser.isPresent()) {
            throw new EntityNotFoundException(
                    "User not present in the database"
            );
        }

        User getUser = optionalUser.get();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        getUser.setPassword(encoder.encode(user.getPassword()));

        return userRepository.save(getUser);
    }

    public User removeUser(User user) {
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
        User getUser = optionalUser.get();
        getUser.setDelYn("Y");
        return userRepository.save(getUser);
    }


    /**
     * Spring Security 필수 메소드 구현
     *
     * @param username 이메일
     * @return UserDetails
     * @throws UsernameNotFoundException 유저가 없을 때 예외 발생
     */
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException((username)));
    }

}


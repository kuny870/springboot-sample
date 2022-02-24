package com.wizvera.templet.service;

import com.wizvera.templet.model.User;
import com.wizvera.templet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 사용자 전체 불러오기
     * @return
     */
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    /**
     * 사용자 한명 불러오기
     * @param loginId
     * @return
     */
    public User getUser(String loginId) {
        Optional<User> user = Optional.ofNullable(userRepository.findByLoginId(loginId));
        if(user.isPresent()) {
            return user.get();
        }

        throw new EntityNotFoundException(
                "Cant find ant user under given ID"
        );
    }

    /**
     * 사용자 저장하기
     * @param saveUser
     * @return
     */
    public User insertUser(User saveUser) {
        return userRepository.save(saveUser);
    }

    /**
     * 사용자 수정하기
     * @param updateUser
     * @return
     */
    public User updateUser(User updateUser) {
        Optional<User> optionalUser = userRepository.findById(updateUser.getId());
        if(!optionalUser.isPresent()) {
            throw new EntityNotFoundException(
                    "User not present in the database"
            );
        }

        User user = optionalUser.get();
        user.builder()
                .name(updateUser.getName())
                .delYn(updateUser.getDelYn())
                .build();

        return userRepository.save(user);
    }

}

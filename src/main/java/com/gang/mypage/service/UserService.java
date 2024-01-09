package com.gang.mypage.service;

import com.gang.mypage.model.UserAccount;
import com.gang.mypage.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    public UserAccount create(UserAccount user) {
        System.out.printf("user %s\n",user.toString());

        if(user == null || user.userId() == null ) {
            throw new RuntimeException("Invalid arguments");
        }

        final String userId = user.userId();
        if(userRepository.existsByUserId(userId)) {
            log.warn("이미 존재하는 아이디입니다. {}", userId);
            throw new RuntimeException("아이디가 이미 존재합니다.");
        }
        System.out.printf("검증 완료\n");
        return userRepository.save(user);
    }

    public UserAccount getByCredentials(final String userId, final String password) {
        return userRepository.findByUserIdAndPassword(userId, password);
    }
}
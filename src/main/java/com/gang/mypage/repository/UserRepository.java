package com.gang.mypage.repository;

import com.gang.mypage.model.UserAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface UserRepository extends CrudRepository<UserAccount, Long> {
    UserAccount findByUserId(String userId);
    boolean existsByUserId(String userId);
    boolean existsById(Long id);

    UserAccount findByUserIdAndPassword(String userId, String password);
}

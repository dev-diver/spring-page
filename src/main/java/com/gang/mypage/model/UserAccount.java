package com.gang.mypage.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Builder
public record UserAccount(
        @Id Long id,
        @NonNull String userId,
        String password, //OAuth로 SSO를 구현하는 경우 password는 null이여도 됨.
        String role,
        String authProvider
) {
}
package com.gang.mypage.model;

import lombok.Builder;
import org.springframework.data.annotation.Id;

@Builder
public record Article(@Id Long id, String userId, String title, String text) {
}
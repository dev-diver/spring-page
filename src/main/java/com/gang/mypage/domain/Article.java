package com.gang.mypage.domain;

import org.springframework.stereotype.Component;

public record Article(Long id, String title, String text) {
}

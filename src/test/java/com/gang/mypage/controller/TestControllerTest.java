package com.gang.mypage.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TestControllerTest {

//    @Autowired
//    private JacksonTester<Article> json;
//
//    @Test
//    void ArticleSerializationTest() throws IOException {
//        Article cashCard = new Article(99L, "제목", "내용");
//        assertThat(json.write(cashCard)).isStrictlyEqualToJson("expected.json");
//        assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.id");
//        assertThat(json.write(cashCard)).extractingJsonPathNumberValue("@.id")
//                .isEqualTo(99);
//        assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.title");
//        assertThat(json.write(cashCard)).extractingJsonPathNumberValue("@.title")
//                .isEqualTo("제목");
//
//    }
}
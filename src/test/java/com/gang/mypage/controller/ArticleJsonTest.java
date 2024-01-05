package com.gang.mypage.controller;

import com.gang.mypage.model.ArticleEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ArticleJsonTest {

    @Autowired
    private JacksonTester<ArticleEntity> json;

    @Test
    void articleSerializationTest() throws IOException {
        ArticleEntity article = new ArticleEntity("post-id", "user-id", "제목", "내용");

        assertThat(json.write(article)).isStrictlyEqualToJson("/mypage/article/exp.json");
        assertThat(json.write(article)).hasJsonPathStringValue("@.id");
        assertThat(json.write(article)).extractingJsonPathStringValue("@.id")
                .isEqualTo("post-id");
        assertThat(json.write(article)).hasJsonPathStringValue("@.title");
        assertThat(json.write(article)).extractingJsonPathStringValue("@.title")
                .isEqualTo("제목");
    }

    @Test
    void articleDeserializationTest() throws IOException {
        String expected = """
            {
              "id": "post-id",
              "userId": "user-id",
              "title": "제목",
              "text" : "내용"
            }
           """;
        assertThat(json.parse(expected))
                .isEqualTo(new ArticleEntity("post-id", "user-id", "제목", "내용"));
        assertThat(json.parseObject(expected).getId()).isEqualTo("post-id");
        assertThat(json.parseObject(expected).getUserId()).isEqualTo("user-id");
        assertThat(json.parseObject(expected).getTitle()).isEqualTo("제목");
        assertThat(json.parseObject(expected).getText()).isEqualTo("내용");

    }
}
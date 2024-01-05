package com.gang.mypage.controller;

import com.gang.mypage.model.Article;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ArticleJsonTest {

    @Autowired
    private JacksonTester<Article> json;

    @Test
    void articleSerializationTest() throws IOException {
        Article article = new Article(99L, "user-id", "제목", "내용");

        assertThat(json.write(article)).isStrictlyEqualToJson("/mypage/article/exp.json");
        assertThat(json.write(article)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(article)).extractingJsonPathNumberValue("@.id")
                .isEqualTo(99);
        assertThat(json.write(article)).hasJsonPathStringValue("@.title");
        assertThat(json.write(article)).extractingJsonPathStringValue("@.title")
                .isEqualTo("제목");
    }

    @Test
    void articleDeserializationTest() throws IOException {
        String expected = """
            {
              "id": 99,
              "userId": "user-id",
              "title": "제목",
              "text" : "내용"
            }
           """;
        assertThat(json.parse(expected))
                .isEqualTo(new Article(99L, "user-id", "제목", "내용"));
        assertThat(json.parseObject(expected).id()).isEqualTo(99);
        assertThat(json.parseObject(expected).userId()).isEqualTo("user-id");
        assertThat(json.parseObject(expected).title()).isEqualTo("제목");
        assertThat(json.parseObject(expected).text()).isEqualTo("내용");

    }
}
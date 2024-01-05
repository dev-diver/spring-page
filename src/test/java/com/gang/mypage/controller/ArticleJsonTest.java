package com.gang.mypage.controller;

import com.gang.mypage.model.Article;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
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

    @Autowired
    private JacksonTester<Article[]> jsonList;

    private Article[] articles;
    @BeforeEach
    void setUp() {
        articles = Arrays.array(
                new Article(99L, "user-id","제목1","내용"),
                new Article(98L, "user-id","제목2","내용"),
                new Article(97L, "user-id","제목3","내용")
        );
    }
    @Test
    void articleSerializationTest() throws IOException {
        Article article = new Article(99L, "user-id", "제목", "내용");

        assertThat(json.write(article)).isStrictlyEqualToJson("/mypage/article/single.json");
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

    @Test
    void articleListSerializationTest() throws IOException {
        assertThat(jsonList.write(articles)).isStrictlyEqualToJson("/mypage/article/list.json");
    }

    @Test
    void cashCardListDeserializationTest() throws IOException {
        String expected="""
        [
          {
            "id": 99,
            "userId": "user-id",
            "title": "제목1",
            "text" : "내용"
          },
          {
            "id": 98,
            "userId": "user-id",
            "title": "제목2",
            "text" : "내용"
          },
          {
            "id": 97,
            "userId": "user-id",
            "title": "제목3",
            "text" : "내용"
          }
        ]
         """;
        assertThat(jsonList.parse(expected)).isEqualTo(articles);
    }
}
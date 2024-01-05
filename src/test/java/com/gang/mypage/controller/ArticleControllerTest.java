package com.gang.mypage.controller;

import com.gang.mypage.model.Article;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void shouldReturnArticleWhenDataIsSaved() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/article/99", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number id = documentContext.read("$.id");
        assertThat(id).isEqualTo(99);

        String title = documentContext.read("$.title");
        assertThat(title).isEqualTo("제목");
    }

    @Test
    void shouldCreateANewArticle() {
        Article newArticle = new Article(null, "123","제목", "내용");
        ResponseEntity<Void> createResponse = restTemplate.postForEntity("/api/article", newArticle, Void.class);

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI locationOfNewArticle = createResponse.getHeaders().getLocation();
        ResponseEntity<String> getResponse = restTemplate.getForEntity(locationOfNewArticle, String.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        Number id = documentContext.read("$.id");
        String title = documentContext.read("$.title");
        String text = documentContext.read("$.text");

        assertThat(id).isNotNull();
        assertThat(title).isEqualTo("제목");
        assertThat(text).isEqualTo("내용");
    }


}
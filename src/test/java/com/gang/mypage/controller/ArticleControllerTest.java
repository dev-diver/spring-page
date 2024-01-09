package com.gang.mypage.controller;

import com.gang.mypage.model.Article;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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
        assertThat(title).isEqualTo("제목1");
    }

    @Test
    @DirtiesContext
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

    @Test
    @DirtiesContext
    void shouldReturnAllArticlesWhenListIsRequested() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/article", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int articleCount = documentContext.read("$.length()");
        assertThat(articleCount).isEqualTo(3);

        JSONArray ids = documentContext.read("$..id");
        assertThat(ids).containsExactlyInAnyOrder(99, 98, 97);

        JSONArray amounts = documentContext.read("$..title");
        assertThat(amounts).containsExactlyInAnyOrder("제목1", "제목2", "제목3");

    }

    @Test
    void shouldReturnAPageOfArticle() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/article?page=0&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray page = documentContext.read("$[*]");
        assertThat(page.size()).isEqualTo(1);
    }

    @Test
    void shouldReturnASortedPageOfArticles() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/article?page=0&size=1&sort=title,desc", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray read = documentContext.read("$[*]");
        assertThat(read.size()).isEqualTo(1);

        String title = documentContext.read("$[0].title");
        assertThat(title).isEqualTo("제목3");
    }

    @Test
    void shouldReturnASortedPageOfArticlesWithNoParametersAndUseDefaultValues() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/article", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray page = documentContext.read("$[*]");
        assertThat(page.size()).isEqualTo(3);

        JSONArray title = documentContext.read("$..id");
        assertThat(title).containsExactly(99, 98, 97);
    }

    @Test
    @DirtiesContext
    void shouldUpdateAnExistingArticle() {
        Article articleUpdate = new Article(null, "user-id2", "제목4","냉무");
        HttpEntity<Article> request = new HttpEntity<>(articleUpdate);
        ResponseEntity<Void> response = restTemplate
                //.withBasicAuth("sarah1", "abc123")
                .exchange("/api/article/99", HttpMethod.PUT, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> getResponse = restTemplate
                //.withBasicAuth("sarah1", "abc123")
                .getForEntity("/api/article/99", String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        Number id = documentContext.read("$.id");
        String title = documentContext.read("$.title");
        assertThat(id).isEqualTo(99);
        assertThat(title).isEqualTo("제목4");
    }

    @Test
    void shouldNotUpdateArticleThatDoesNotExist() {
        Article unknownArticle = new Article(null, "user-id", "제목123", "냉무");
        HttpEntity<Article> request = new HttpEntity<>(unknownArticle);
        ResponseEntity<Void> response = restTemplate
                //.withBasicAuth("sarah1", "abc123")
                .exchange("/api/article/99999", HttpMethod.PUT, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

//    @Test
//    void shouldNotUpdateACashCardThatIsOwnedBySomeoneElse() {
//        Article kumarsArticle = new Article(null, "user-id3", "제목이당", "냉무무");
//        HttpEntity<Article> request = new HttpEntity<>(kumarsArticle);
//        ResponseEntity<Void> response = restTemplate
//                .withBasicAuth("sarah1", "abc123")
//                .exchange("/api/article/102", HttpMethod.PUT, request, Void.class);
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//    }

    @Test
    @DirtiesContext
    void shouldDeleteAnExistingArticle() {
        ResponseEntity<Void> response = restTemplate
                //.withBasicAuth("sarah1", "abc123")
                .exchange("/api/article/99", HttpMethod.DELETE, null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        System.out.println("well deleted");
        ResponseEntity<String> getResponse = restTemplate
                //.withBasicAuth("sarah1", "abc123")
                .getForEntity("/api/article/99", String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DirtiesContext
    void shouldNotDeleteArticleThatDoesNotExist() {
        ResponseEntity<Void> deleteResponse = restTemplate
                //.withBasicAuth("sarah1", "abc123")
                .exchange("/api/article/99999", HttpMethod.DELETE, null, Void.class);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

//    @Test
//    void shouldNotAllowDeletionOfCashCardsTheyDoNotOwn() {
//        ResponseEntity<Void> deleteResponse = restTemplate
//                .withBasicAuth("sarah1", "abc123")
//                .exchange("/cashcards/102", HttpMethod.DELETE, null, Void.class);
//        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

//        ResponseEntity<String> getResponse = restTemplate
//                .withBasicAuth("kumar2", "xyz789")
//                .getForEntity("/cashcards/102", String.class);
//        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//    }

}
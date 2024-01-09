package com.gang.mypage.controller;

import com.gang.mypage.dto.ArticleDTO;
import com.gang.mypage.dto.UserDTO;
import com.gang.mypage.model.Article;
import com.gang.mypage.repository.ArticleRepository;
import com.gang.mypage.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("api/article")
public class ArticleController {

    @Autowired
    private ArticleService service;

    @Autowired
    private ArticleRepository articleRepository;

    private ArticleController(ArticleRepository articleRepository){
        this.articleRepository = articleRepository;
    }

    @GetMapping
    private ResponseEntity<List<Article>> findAll(Pageable pageable) {
        Page<Article> page = articleRepository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
//                        pageable.getSort(), //sort기준은 argument로 넘어온다.
                        pageable.getSortOr(Sort.by(Sort.Direction.DESC, "id"))
                ));
        return ResponseEntity.ok(page.getContent());
    }

    @PostMapping
    public ResponseEntity<?> createArticle(@AuthenticationPrincipal UserDTO userDto, @RequestBody ArticleDTO dto, UriComponentsBuilder ucb){
        dto.setUserId(null);
        dto.setUserId(userDto.getId());
        Article entity = ArticleDTO.toEntity(dto);

        Article savedArticle = articleRepository.save(entity);
        URI locationOfNewArticle = ucb
                .path("/api/article/{id}")
                .buildAndExpand(savedArticle.id())
                .toUri();

        return ResponseEntity.created(locationOfNewArticle).build();
    }

    @GetMapping("/{requestedId}")
    public ResponseEntity<Article> articleFindById(@PathVariable Long requestedId){
        Optional<Article> articleOptional = articleRepository.findById(requestedId);
        if (articleOptional.isPresent()) {
            return ResponseEntity.ok(articleOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{requestedId}")
    private ResponseEntity<Void> putArticle(@AuthenticationPrincipal UserDTO userDto, @PathVariable Long requestedId, @RequestBody Article articleUpdate) {

        log.info("article ID {}  userId {}\n", requestedId, userDto.getId());
        Optional<Article> optionalArticle = articleRepository.findByIdAndUserId(requestedId, userDto.getId());
        if(optionalArticle.isPresent()){
            Article article = optionalArticle.get();
            Article updatedArticle = new Article(article.id(), userDto.getId() , articleUpdate.title(), articleUpdate.text());
            articleRepository.save(updatedArticle);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{requestedId}")
    private ResponseEntity<Void> deleteArticle(@AuthenticationPrincipal UserDTO userDto, @PathVariable Long requestedId) {
        //findById 써도 되는데 쓸데없는 정보가 있을 수 있음
        if (articleRepository.existsByIdAndUserId(requestedId, userDto.getId())){
            articleRepository.deleteById(requestedId); // Add this line
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}
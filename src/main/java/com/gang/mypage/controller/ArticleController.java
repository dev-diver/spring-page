package com.gang.mypage.controller;

import com.gang.mypage.dto.ArticleDTO;
import com.gang.mypage.model.Article;
import com.gang.mypage.repository.ArticleRepository;
import com.gang.mypage.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "title"))
                ));
        return ResponseEntity.ok(page.getContent());
    }

    @PostMapping
    public ResponseEntity<?> createArticle(@RequestBody ArticleDTO dto, UriComponentsBuilder ucb){
        Article entity = ArticleDTO.toEntity(dto);
        System.out.println(entity.toString());
        Article savedArticle = articleRepository.save(entity);
        URI locationOfNewArticle = ucb
                .path("/api/article/{id}")
                .buildAndExpand(savedArticle.id())
                .toUri();

        return ResponseEntity.created(locationOfNewArticle).build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Article> articleFindById(@PathVariable(required = false) Long id){
        Optional<Article> articleOptional = articleRepository.findById(id);
        if (articleOptional.isPresent()) {
            return ResponseEntity.ok(articleOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
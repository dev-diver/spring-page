package com.gang.mypage.controller;

import com.gang.mypage.dto.ArticleDTO;
import com.gang.mypage.dto.ResponseDTO;
import com.gang.mypage.model.ArticleEntity;
import com.gang.mypage.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/board")
public class ArticleController {

    @Autowired
    private ArticleService service;

    @GetMapping
    public String ArticleController(){
        return "Hello World!";
    }

    @GetMapping("/{id}")
    public String articleControllerWithId(@PathVariable(required= false) Long id){
        return "Hello World!" + id;
    }

    @PostMapping
    public ResponseEntity<?> createArticle(@RequestBody ArticleDTO dto){
        try{
            String temporaryUserId = "temporary-user";

            ArticleEntity entity = ArticleDTO.toEntity(dto);
            entity.setId(null);
            entity.setUserId(temporaryUserId);
            List<ArticleEntity> entities = service.create(entity);
            List<ArticleDTO> dtos = entities.stream().map(ArticleDTO::new).collect(Collectors.toList());

            ResponseDTO<ArticleDTO> response = ResponseDTO.<ArticleDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        }catch(Exception e){
            String error = e.getMessage();
            ResponseDTO<ArticleDTO> response = ResponseDTO.<ArticleDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}
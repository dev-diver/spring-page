package com.gang.mypage.service;

import com.gang.mypage.model.ArticleEntity;
import com.gang.mypage.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class ArticleService {

    @Autowired
    private ArticleRepository repository;

    public List<ArticleEntity> create(final ArticleEntity entity){
        validate(entity);
        repository.save(entity);
        log.info("Entity ID : {} is saved", entity.getId());
        return Collections.singletonList(repository.findById(entity.getId()).get());
    }

    private void validate(final ArticleEntity entity){
        if(entity == null){
            log.warn("Entity cannot be null");
            throw new RuntimeException("Entity cannot be null");
        }

    }
}

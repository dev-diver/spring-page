package com.gang.mypage.repository;

import com.gang.mypage.model.Article;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;


public interface ArticleRepository extends CrudRepository<Article, Long> , PagingAndSortingRepository<Article, Long>{
    boolean existsById(Long id);
    boolean existsByIdAndUserId(Long id, Long userId);

    Optional<Article> findByIdAndUserId(Long requestedId, Long userId);
}
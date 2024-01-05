package com.gang.mypage.repository;

import com.gang.mypage.model.Article;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface ArticleRepository extends CrudRepository<Article, Long> , PagingAndSortingRepository<Article, Long>{
}
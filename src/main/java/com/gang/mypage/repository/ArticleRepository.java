package com.gang.mypage.repository;

import com.gang.mypage.model.Article;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface ArticleRepository extends CrudRepository<Article, Long> {
}
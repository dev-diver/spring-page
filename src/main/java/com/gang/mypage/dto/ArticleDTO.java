package com.gang.mypage.dto;

import com.gang.mypage.model.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ArticleDTO {
    private Long id;
    private String title;
    private String text;
    private Long userId;

    public ArticleDTO(final Article entity){
        this.id = entity.id();
        this.title = entity.title();
        this.text = entity.text();
    }

    public static Article toEntity(final ArticleDTO dto){
        return Article.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .text(dto.getText())
                .userId(dto.getUserId())
                .build();
    }

}

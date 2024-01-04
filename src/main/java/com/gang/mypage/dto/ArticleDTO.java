package com.gang.mypage.dto;

import com.gang.mypage.model.ArticleEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ArticleDTO {
    private String id;
    private String title;
    private String text;

    public ArticleDTO(final ArticleEntity entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.text = entity.getText();
    }

    public static ArticleEntity toEntity(final ArticleDTO dto){
        return ArticleEntity.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .text(dto.getText())
                .build();
    }

}

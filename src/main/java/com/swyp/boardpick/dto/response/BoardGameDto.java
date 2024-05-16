package com.swyp.boardpick.dto.response;

import com.swyp.boardpick.domain.BoardGame;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class BoardGameDto {
    private Long id;
    private String thumbnailUrl;
    private String imageUrl;
    private String name;
    private String description;
    private double rating;
    private int ratingCount;
    public BoardGameDto(BoardGame boardGame) {
        this.id = boardGame.getId();
        this.thumbnailUrl = boardGame.getThumbnailUrl();
        this.imageUrl = boardGame.getImageUrl();
        this.name = boardGame.getName();
        this.description = boardGame.getDescription();
        this.rating = boardGame.getRating();
        this.ratingCount = boardGame.getRatingCount();
    }
}

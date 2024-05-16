package com.swyp.boardpick.dto.response;

import com.swyp.boardpick.domain.BoardGame;
import lombok.Builder;

@Builder
public class BoardGameDto {
    private String thumbnailUrl;
    private String imageUrl;
    private String name;
    private String description;
    private double rating;
    private int ratingCount;
    public BoardGameDto(BoardGame boardGame) {
         this.thumbnailUrl = boardGame.getThumbnailUrl();
         this.imageUrl = boardGame.getImageUrl();
         this.name = boardGame.getName();
         this.description = boardGame.getDescription();
         this.rating = boardGame.getRating();
         this.ratingCount = boardGame.getRatingCount();
    }
}

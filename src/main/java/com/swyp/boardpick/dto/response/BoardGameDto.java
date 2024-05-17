package com.swyp.boardpick.dto.response;

import com.swyp.boardpick.domain.BoardGame;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

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
    private boolean picked;
    private List<String> tags;
    public BoardGameDto(BoardGame boardGame, List<String> tags) {
        this.id = boardGame.getId();
        this.thumbnailUrl = boardGame.getThumbnailUrl();
        this.imageUrl = boardGame.getImageUrl();
        this.name = boardGame.getName();
        this.description = boardGame.getDescription();
        this.rating = boardGame.getRating();
        this.ratingCount = boardGame.getRatingCount();
        this.tags = tags;
        this.picked = true;
    }

    public BoardGameDto(BoardGame boardGame, List<String> tags, boolean picked) {
        this.id = boardGame.getId();
        this.thumbnailUrl = boardGame.getThumbnailUrl();
        this.imageUrl = boardGame.getImageUrl();
        this.name = boardGame.getName();
        this.description = boardGame.getDescription();
        this.rating = boardGame.getRating();
        this.ratingCount = boardGame.getRatingCount();
        this.tags = tags;
        this.picked = picked;
    }
}

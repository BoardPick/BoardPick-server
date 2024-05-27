package com.swyp.boardpick.dto.response;

import com.swyp.boardpick.domain.*;
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
    private int minPlayers;
    private int maxPlayers;
    private int playtime;
    private int ageLimit;
    private String difficulty;
    private String rule;
    private String extraVideo;
    private int likes;
    private boolean picked;
    private List<String> boardGameCategories;
    private List<UserBoardGame> userBoardGames;
    private List<String> tags;

    public BoardGameDto(BoardGame boardGame, boolean picked) {
        this.id = boardGame.getId();
        this.thumbnailUrl = boardGame.getThumbnailUrl();
        this.imageUrl = boardGame.getImageUrl();
        this.name = boardGame.getName();
        this.description = boardGame.getDescription();
        this.rating = boardGame.getRating();
        this.ratingCount = boardGame.getRatingCount();
        this.minPlayers = boardGame.getMinPlayers();
        this.maxPlayers = boardGame.getMaxPlayers();
        this.playtime = boardGame.getPlaytime();
        this.ageLimit = boardGame.getAgeLimit();
        this.difficulty = convertDifficulty(boardGame.getDifficulty()).getDescription();
        this.rule = boardGame.getRule();
        this.extraVideo = boardGame.getExtraVideo();
        this.likes = boardGame.getLikes();
        this.boardGameCategories = boardGame.getBoardGameCategories()
                .stream().map(boardGameCategory -> boardGameCategory.getCategory().getType())
                .toList();
        this.userBoardGames = boardGame.getUserBoardGames();
        this.tags = boardGame.getBoardGameTags()
                .stream().map(boardGameTag -> boardGameTag.getTag().getContent())
                .toList();
        this.picked = picked;
    }

    private static Difficulty convertDifficulty(double difficulty) {
        if (difficulty < 1.8)
            return Difficulty.VERY_EASY;
        if (difficulty < 2.6)
            return Difficulty.EASY;
        if (difficulty < 3.4)
            return Difficulty.NORMAL;
        if (difficulty < 4.2)
            return Difficulty.HARD;
        return Difficulty.VERY_HARD;
    }
}


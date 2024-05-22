package com.swyp.boardpick.dto.response;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.swyp.boardpick.domain.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
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

    public BoardGameDto(BoardGame boardGame, String difficulty, List<String> boardGameCategories, List<String> tags, boolean picked) {
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
        this.difficulty = difficulty;
        this.rule = boardGame.getRule();
        this.extraVideo = boardGame.getExtraVideo();
        this.likes = boardGame.getLikes();
        this.boardGameCategories = boardGameCategories;
        this.userBoardGames = boardGame.getUserBoardGames();
        this.tags = tags;
        this.picked = picked;
    }
}


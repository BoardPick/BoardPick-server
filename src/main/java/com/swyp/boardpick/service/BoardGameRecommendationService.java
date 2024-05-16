package com.swyp.boardpick.service;

import com.swyp.boardpick.domain.BoardGame;

import java.util.List;

public interface BoardGameRecommendationService {
    List<BoardGame> recommendBoardGames();
}

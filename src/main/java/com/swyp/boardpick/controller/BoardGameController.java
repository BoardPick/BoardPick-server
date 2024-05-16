package com.swyp.boardpick.controller;

import com.swyp.boardpick.domain.BoardGame;
import com.swyp.boardpick.service.BoardGameRecommendationService;
import com.swyp.boardpick.service.implement.BoardGameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boardgames")
public class BoardGameController {

    private final BoardGameService boardGameService;
    private final BoardGameRecommendationService recommendationService;

    @GetMapping("/{id}")
    public ResponseEntity<BoardGame> getBoardGameById(@PathVariable Long id) {
        return boardGameService.getBoardGameById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/recommendations")
    public ResponseEntity<List<BoardGame>> getRecommendations() {
        List<BoardGame> recommendedBoardGames = recommendationService.recommendBoardGames();
        if (recommendedBoardGames.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(recommendedBoardGames);
    }
}

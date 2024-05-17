package com.swyp.boardpick.controller;

import com.swyp.boardpick.domain.BoardGame;
import com.swyp.boardpick.dto.response.BoardGameDto;
import com.swyp.boardpick.service.BoardGameRecommendationService;
import com.swyp.boardpick.service.implement.BoardGameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    @ResponseBody
    public List<BoardGameDto> getBoardgamesByCategory(
            @RequestParam String category, @RequestParam int page, @RequestParam int size) {
        return boardGameService.getBoardGamesByCategory(category, page, size);
    }

    @GetMapping("/today-pick")
    public List<BoardGameDto> getTodayPick() {
        return boardGameService.getTodayPick();
    }
}

package com.swyp.boardpick.controller;

import com.swyp.boardpick.dto.response.BoardGameDto;
import com.swyp.boardpick.service.implement.BoardGameService;
import com.swyp.boardpick.service.implement.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boardgames")
public class BoardGameController {

    private final BoardGameService boardGameService;
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<BoardGameDto> getBoardGameById(@PathVariable Long id) {
        return boardGameService.getBoardGameById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/recs")
    public ResponseEntity<List<BoardGameDto>> getRecommendations() {
        List<BoardGameDto> recommendedBoardGames = boardGameService.recommendBoardGames();
        if (recommendedBoardGames.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(recommendedBoardGames);
    }

    @GetMapping
    @ResponseBody
    public List<BoardGameDto> getBoardgamesByCategory(
            @RequestParam String category, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return boardGameService.getBoardGamesByCategory(category, page, size);
    }

    @GetMapping("/search")
    public List<BoardGameDto> searchBoardGames(
            @RequestParam String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return boardGameService.searchBoardGamesByKeyword(keyword, page, size);
    }

    @GetMapping("/today-pick")
    public List<BoardGameDto> getTodayPick() {
        return boardGameService.getTodayPick();
    }

    @GetMapping("/list")
    public List<BoardGameDto> getTop10(@RequestParam String filter) {
        return boardGameService.getTop10(filter);
    }

    @GetMapping("similar/{id}")
    public List<BoardGameDto> similarBoardGames(@PathVariable Long id) {
        return boardGameService.getSimilarBoardGamesById(id);
    }
}

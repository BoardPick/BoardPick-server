package com.swyp.boardpick.controller;

import com.swyp.boardpick.domain.BoardGame;
import com.swyp.boardpick.dto.response.BoardGameDto;
import com.swyp.boardpick.service.implement.BoardGameService;
import com.swyp.boardpick.service.implement.UserBoardGameService;
import com.swyp.boardpick.service.implement.UserService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boardgames")
public class BoardGameController {

    private final BoardGameService boardGameService;
    private final UserBoardGameService userBoardGameService;
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<BoardGameDto> getBoardGameById(@PathVariable Long id, Authentication principal) {

        BoardGame boardGame = boardGameService.getBoardGameById(id);
        boolean picked = false;

        if (principal != null) {
            Long userId = userService.getUserId(principal.getName());
            picked = userBoardGameService.getPicked(userId, id);
        }

        return ResponseEntity.ok(new BoardGameDto(boardGame, picked));
    }

    @GetMapping("/recs")
    public ResponseEntity<List<BoardGameDto>> getRecommendations(Authentication principal) {

        if (principal == null) {
            return ResponseEntity.ok(
                    boardGameService.convertToDotListForAnonymous(
                            boardGameService.getRandomBoardGames()
                    ));
        }

        Long userId = userService.getUserId(principal.getName());
        List<BoardGame> recommendedBoardGames = boardGameService.getRecommendationBoardGamesByUser(userId);

        if (recommendedBoardGames.size() < 10) {

            List<BoardGame> randomBoardGames = boardGameService.getRandomBoardGames();
            randomBoardGames.removeAll(recommendedBoardGames);

            int remainingSlots = 10 - recommendedBoardGames.size();
            recommendedBoardGames.addAll(randomBoardGames.stream().limit(remainingSlots).toList());
        }

        return ResponseEntity.ok(boardGameService.convertToDtoList(recommendedBoardGames, userId));
    }

    @GetMapping("/suggestion")
    public ResponseEntity<List<BoardGameDto>> getSuggestionBoardGames() {
        return ResponseEntity.ok(
                boardGameService.convertToDotListForAnonymous(
                boardGameService.getPopularBoardGames()
        ));
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<BoardGameDto>> getBoardgamesByCategory(Authentication principal,
            @RequestParam String category, @RequestParam(defaultValue = "0") @Min(0) int page, @RequestParam(defaultValue = "1000") @Min(1) int size) {

        List<BoardGame> boardGames = boardGameService.getBoardGamesByCategory(category, page, size);

        if (boardGames.isEmpty())
            return ResponseEntity.noContent().build();

        if (principal == null) {
            return ResponseEntity.ok(boardGameService.convertToDotListForAnonymous(boardGames));
        }

        Long userId = userService.getUserId(principal.getName());

        return ResponseEntity.ok(boardGameService.convertToDtoList(boardGames, userId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<BoardGameDto>> searchBoardGames(Authentication principal,
            @RequestParam String keyword, @RequestParam(defaultValue = "0") @Min(0) int page, @RequestParam(defaultValue = "1000") @Min(1) int size) {

        List<BoardGame> boardGames = boardGameService.searchBoardGamesByKeyword(keyword, page, size);

        if (boardGames.isEmpty())
            return ResponseEntity.noContent().build();

        if (principal == null) {
            return ResponseEntity.ok(boardGameService.convertToDotListForAnonymous(boardGames));
        }

        Long userId = userService.getUserId(principal.getName());

        return ResponseEntity.ok(boardGameService.convertToDtoList(boardGames, userId));
    }

    @GetMapping("/today-pick")
    public ResponseEntity<List<BoardGameDto>> getTodayPick(Authentication principal) {

        List<BoardGame> boardGames = boardGameService.getTodayPick();

        if (boardGames.isEmpty())
            return ResponseEntity.noContent().build();

        if (principal == null) {
            return ResponseEntity.ok(boardGameService.convertToDotListForAnonymous(boardGames));
        }

        Long userId = userService.getUserId(principal.getName());

        return ResponseEntity.ok(boardGameService.convertToDtoList(boardGames, userId));
    }

    @GetMapping("/list")
    public ResponseEntity<List<BoardGameDto>> getTop10(@RequestParam String filter, Authentication principal) {

        List<BoardGame> boardGames = boardGameService.getTop10(filter);

        if (boardGames.isEmpty())
            return ResponseEntity.noContent().build();

        if (principal == null) {
            return ResponseEntity.ok(boardGameService.convertToDotListForAnonymous(boardGames));
        }

        Long userId = userService.getUserId(principal.getName());

        return ResponseEntity.ok(boardGameService.convertToDtoList(boardGames, userId));
    }

    @GetMapping("/similar/{id}")
    public ResponseEntity<List<BoardGameDto>> similarBoardGames(@PathVariable Long id, Authentication principal) {

        List<BoardGame> boardGames = boardGameService.getSimilarBoardGamesById(id);

        if (boardGames.isEmpty())
            return ResponseEntity.noContent().build();

        if (principal == null) {
            return ResponseEntity.ok(boardGameService.convertToDotListForAnonymous(boardGames));
        }

        Long userId = userService.getUserId(principal.getName());

        return ResponseEntity.ok(boardGameService.convertToDtoList(boardGames, userId));
    }
}

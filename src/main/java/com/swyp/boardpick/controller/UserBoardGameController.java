package com.swyp.boardpick.controller;

import com.swyp.boardpick.domain.BoardGame;
import com.swyp.boardpick.dto.response.BoardGameDto;
import com.swyp.boardpick.service.implement.BoardGameService;
import com.swyp.boardpick.service.implement.UserBoardGameService;
import com.swyp.boardpick.service.implement.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/pick")
@RequiredArgsConstructor
public class UserBoardGameController {

    private final UserBoardGameService userBoardGameService;
    private final UserService userService;
    private final BoardGameService boardGameService;

    @PostMapping("/{boardGameId}")
    public ResponseEntity<?> togglePick(@PathVariable("boardGameId") Long boardGameId, Authentication principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId = userService.getUserId(principal.getName());

        boolean picked = userBoardGameService.togglePick(userId, boardGameId);

        return ResponseEntity.ok().body(Map.of("picked", picked));
    }

    @GetMapping
    public ResponseEntity<List<BoardGameDto>> getMyPickList(Authentication principal) {
        if (principal == null) {
            return ResponseEntity.noContent().build();
        }

        Long userId = userService.getUserId(principal.getName());
        List<BoardGame> boardGames = userBoardGameService.getMyPickList(userId);

        if (boardGames.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(boardGameService.convertToDtoList(boardGames, userId));
    }

    @GetMapping("/get/{boardGameId}")
    public ResponseEntity<?> getPicked(@PathVariable("boardGameId") Long boardGameId, Authentication principal) {
        if (principal == null)
            return ResponseEntity.ok().body(Map.of("picked", false));

        Long userId = userService.getUserId(principal.getName());
        boolean picked = userBoardGameService.getPicked(userId, boardGameId);

        return ResponseEntity.ok().body(Map.of("picked", picked));
    }

    @GetMapping("/get-ids")
    public ResponseEntity<List<Long>> getPickedIds(Authentication principal) {
        if (principal == null)
            return ResponseEntity.noContent().build();

        Long userId = userService.getUserId(principal.getName());
        List<Long> ids = userBoardGameService.getMyPickIds(userId);

        return ResponseEntity.ok(ids);
    }
}

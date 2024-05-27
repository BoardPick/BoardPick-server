package com.swyp.boardpick.controller;

import com.swyp.boardpick.domain.BoardGame;
import com.swyp.boardpick.domain.CustomOAuth2User;
import com.swyp.boardpick.domain.User;
import com.swyp.boardpick.dto.response.BoardGameDto;
import com.swyp.boardpick.repository.UserRepository;
import com.swyp.boardpick.service.implement.BoardGameService;
import com.swyp.boardpick.service.implement.UserBoardGameService;
import com.swyp.boardpick.service.implement.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final UserRepository userRepository;
    private final BoardGameService boardGameService;

    @PostMapping("/{boardGameId}")
    public ResponseEntity<?> togglePick(@PathVariable("boardGameId") Long boardGameId, Authentication principal) {
        if (principal == null) {
//            URI uri = URI.create(Uri.LOGIN_PAGE.getDescription());
//            return ResponseEntity.status(HttpStatus.FOUND).location(uri).build();
            System.out.println("principal is null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId = userService.getUserId(principal.getName());

        boolean picked = userBoardGameService.togglePick(userId, boardGameId);

        return ResponseEntity.ok().body(Map.of("picked", picked));
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<BoardGameDto>> getMyPickList(Authentication principal) {
        if (principal == null) {
//            URI uri = URI.create(Uri.LOGIN_PAGE.getDescription());
//            return ResponseEntity.status(HttpStatus.FOUND).location(uri).build();
            return ResponseEntity.noContent().build();
        }

        Long userId = userService.getUserId(principal.getName());
        List<BoardGame> boardGames = userBoardGameService.getMyPickList(userId);

        if (boardGames.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(boardGameService.convertToDtoList(boardGames, userId));
    }
}

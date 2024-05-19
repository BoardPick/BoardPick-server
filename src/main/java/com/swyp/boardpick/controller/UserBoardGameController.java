package com.swyp.boardpick.controller;

import com.swyp.boardpick.domain.CustomOAuth2User;
import com.swyp.boardpick.domain.Uri;
import com.swyp.boardpick.domain.User;
import com.swyp.boardpick.dto.response.BoardGameDto;
import com.swyp.boardpick.repository.UserRepository;
import com.swyp.boardpick.service.implement.UserBoardGameService;
import com.swyp.boardpick.service.implement.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pick")
@RequiredArgsConstructor
public class UserBoardGameController {

    private final UserBoardGameService userBoardGameService;
    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/{boardGameId}")
    public ResponseEntity<?> togglePick(@PathVariable("boardGameId") Long boardGameId, @AuthenticationPrincipal CustomOAuth2User principal) {

        if (principal == null) {
//            URI uri = URI.create(Uri.LOGIN_PAGE.getDescription());
//            return ResponseEntity.status(HttpStatus.FOUND).location(uri).build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String userCode = principal.getName();
        User user = userRepository.findByCode(userCode)
                .orElseThrow(() -> new EntityNotFoundException("User not found with code: " + userCode));
        Long userId = user.getId();

        boolean isPicked = userBoardGameService.togglePick(userId, boardGameId);

        return ResponseEntity.ok().body(Map.of("picked", isPicked));
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<BoardGameDto>> getMyPickList(@AuthenticationPrincipal CustomOAuth2User principal) {
        if (principal == null) {
//            URI uri = URI.create(Uri.LOGIN_PAGE.getDescription());
//            return ResponseEntity.status(HttpStatus.FOUND).location(uri).build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long id = userService.getUserId(principal.getName());
        return ResponseEntity
                .ok(userService.getMyPickList(id));
    }
}


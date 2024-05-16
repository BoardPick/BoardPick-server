package com.swyp.boardpick.controller;

import com.swyp.boardpick.domain.CustomOAuth2User;
import com.swyp.boardpick.domain.User;
import com.swyp.boardpick.repository.UserRepository;
import com.swyp.boardpick.service.implement.UserBoardGameService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/pick")
@RequiredArgsConstructor
public class UserBoardGameController {

    private final UserBoardGameService userBoardGameService;
    private final UserRepository userRepository;

    @PostMapping("/{boardGameId}")
    public ResponseEntity<?> togglePick(@PathVariable Long boardGameId, @AuthenticationPrincipal CustomOAuth2User principal) {

        String userCode = principal.getName();
        User user = userRepository.findByCode(userCode)
                .orElseThrow(() -> new EntityNotFoundException("User not found with code: " + userCode));
        Long userId = user.getId();

        boolean isPicked = userBoardGameService.togglePick(userId, boardGameId);

        return ResponseEntity.ok().body(Map.of("picked", isPicked));
    }
}


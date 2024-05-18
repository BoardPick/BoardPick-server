package com.swyp.boardpick.controller;

import com.swyp.boardpick.domain.CustomOAuth2User;
import com.swyp.boardpick.dto.response.UserDto;
import com.swyp.boardpick.service.implement.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserDto> getMyInfo(@AuthenticationPrincipal CustomOAuth2User principal) {
        Long userId = userService.getUserId(principal.getName());
        UserDto userDto = userService.getMyInfo(userId);
        return ResponseEntity.ok(userDto);
    }
}

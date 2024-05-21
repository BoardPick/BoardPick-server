package com.swyp.boardpick.controller;

import com.swyp.boardpick.domain.CustomOAuth2User;
import com.swyp.boardpick.domain.Uri;
import com.swyp.boardpick.dto.response.UserDto;
import com.swyp.boardpick.service.implement.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserDto> getMyInfo(Authentication principal) {
        if (principal == null) {
//            URI uri = URI.create(Uri.LOGIN_PAGE.getDescription());
//            return ResponseEntity.status(HttpStatus.FOUND).location(uri).build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId = userService.getUserId(principal.getName());
        UserDto userDto = userService.getMyInfo(userId);
        return ResponseEntity.ok(userDto);
    }
}

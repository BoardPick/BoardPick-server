package com.swyp.boardpick.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Uri {
    HTTP_FOUND("https://boardpick.netlify.app/onBoarding"),
    MAIN_PAGE("https://boardpick.netlify.app"),
    LOGOUT_REDIR("http://localhost:8080/logout");

    private final String description;
}

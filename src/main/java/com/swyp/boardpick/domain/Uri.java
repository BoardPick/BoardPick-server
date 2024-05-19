package com.swyp.boardpick.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Uri {
    LOGIN_PAGE("https://boardpick.netlify.app/onBoarding"),
    MAIN_PAGE("https://boardpick.netlify.app"),
    LOGOUT_REDIR("https://boardpick-server.store/logout");

    private final String description;
}

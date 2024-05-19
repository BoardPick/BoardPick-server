package com.swyp.boardpick.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Uri {
    LOGIN_PAGE("https://boardpick.netlify.app/onBoarding"),
    MAIN_PAGE("https://boardpick.netlify.app"),
    LOGOUT_REDIR("http://ec2-13-124-98-35.ap-northeast-2.compute.amazonaws.com/logout");

    private final String description;
}

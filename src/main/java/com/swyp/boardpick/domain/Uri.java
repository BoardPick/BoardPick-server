package com.swyp.boardpick.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Uri {
    HTTP_FOUND("https://boardpick.netlify.app/onBoarding");

    private final String description;
}

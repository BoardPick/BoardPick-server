package com.swyp.boardpick.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Difficulty {
    VERY_EASY("왕초보"),
    EASY("초보"),
    NORMAL("중수"),
    HARD("고수"),
    VERY_HARD("초고수");

    private final String description;
}

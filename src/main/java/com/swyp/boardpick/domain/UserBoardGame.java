package com.swyp.boardpick.domain;

import jakarta.persistence.*;

@Entity
public class UserBoardGame {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardgame_id")
    private BoardGame boardgame;
}

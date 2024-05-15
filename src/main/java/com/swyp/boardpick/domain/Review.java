package com.swyp.boardpick.domain;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Review {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardgame_id")
    private BoardGame boardGame;
    private String content;
    private int stars;
    private int recommendations;
    private LocalDate date;
}

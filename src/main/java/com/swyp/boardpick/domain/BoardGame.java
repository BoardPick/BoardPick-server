package com.swyp.boardpick.domain;

import jakarta.persistence.*;

@Entity
public class BoardGame {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String rule;
    private int likes;
}

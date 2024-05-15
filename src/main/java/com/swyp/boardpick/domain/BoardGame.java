package com.swyp.boardpick.domain;

import jakarta.persistence.*;

@Entity
public class BoardGame {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String thumbnailUrl;
    private String imageUrl;
    private String name;
    private double rating;
    private int ratingCount;
    private int minPlayers;
    private int maxPlayers;
    private int playtime;
    private int ageLimit;
    private double difficulty;
    private String rule;
    private int likes;
}

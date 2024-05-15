package com.swyp.boardpick.domain;

import jakarta.persistence.*;

@Entity
public class Choice {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private Emotion emotion;
}

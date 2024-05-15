package com.swyp.boardpick.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class BoardGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private String youtubeLink;

//    @OneToMany
//    private List<Tag> tagIds;
//    @OneToMany
//    private List<Category> categoryIds;

}

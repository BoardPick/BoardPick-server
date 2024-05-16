package com.swyp.boardpick.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    @Enumerated(EnumType.ORDINAL)
    private Emotion emotion;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "category")
    List<BoardGameCategory> boardGameCategories = new ArrayList<>();
}

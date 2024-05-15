package com.swyp.boardpick.domain;

import jakarta.persistence.*;


@Entity
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String nickname;
    private Role role;
}

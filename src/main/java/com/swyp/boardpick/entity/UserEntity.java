package com.swyp.boardpick.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="user")
@Table(name="user")
public class UserEntity {
    @Id
    private String userId;
    private String nickname;
    private String role;
    private String type;

    public UserEntity (String userId, String type) {
        this.userId = userId;
        this.type = type;
        this.role = "ROLE_USER";
    }
}

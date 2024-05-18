package com.swyp.boardpick.dto.response;

import com.swyp.boardpick.domain.Role;
import com.swyp.boardpick.domain.User;
import com.swyp.boardpick.domain.UserBoardGame;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String code;
    private String profileImage;
    private String nickname;
    private Role role;
    private List<UserBoardGame> userBoardGames;

    public UserDto(User user) {
        this.id = user.getId();
        this.code = user.getCode();
        this.profileImage = user.getProfileImage();
        this.nickname = user.getNickname();
        this.role = user.getRole();
        this.userBoardGames = user.getUserBoardGames();
    }
}

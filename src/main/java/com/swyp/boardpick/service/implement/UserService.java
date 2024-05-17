package com.swyp.boardpick.service.implement;

import com.swyp.boardpick.domain.BoardGame;
import com.swyp.boardpick.domain.User;
import com.swyp.boardpick.dto.response.BoardGameDto;
import com.swyp.boardpick.repository.BoardGameRepository;
import com.swyp.boardpick.repository.BoardGameTagRepository;
import com.swyp.boardpick.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Long getUserId(String userCode) {
        User user = userRepository.findByCode(userCode)
                .orElseThrow(() -> new EntityNotFoundException("User not found with code: " + userCode));
        return user.getId();
    }

    public Long getCurrentOAuth2UserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuth2AuthenticationToken oauthToken) {

            OAuth2User oauthUser = oauthToken.getPrincipal();
            return getUserId(oauthUser.getName());
        }

        return null;
    }

    public List<BoardGameDto> getMyPickList(Long id) {
        return userRepository.findById(id)
                .get().getUserBoardGames()
                .stream().map(userBoardGame -> {
                    BoardGame boardGame = userBoardGame.getBoardGame();
                    List<String> tags = boardGame.getBoardGameTags()
                            .stream().map(boardGameTag -> boardGameTag.getTag().getContent())
                            .toList();
                    return new BoardGameDto(userBoardGame.getBoardGame(), tags);
                })
                .toList();
    }
}

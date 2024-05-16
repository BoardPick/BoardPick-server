package com.swyp.boardpick.service.implement;

import com.swyp.boardpick.domain.BoardGame;
import com.swyp.boardpick.domain.User;
import com.swyp.boardpick.domain.UserBoardGame;
import com.swyp.boardpick.repository.BoardGameRepository;
import com.swyp.boardpick.repository.UserBoardGameRepository;
import com.swyp.boardpick.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserBoardGameService {

    private final UserBoardGameRepository userBoardGameRepository;
    private final UserRepository userRepository;
    private final BoardGameRepository boardGameRepository;

    @Transactional
    public boolean togglePick(Long userId, Long boardGameId) {

        // 좋아요 취소
        if (userBoardGameRepository.existsByUserIdAndBoardGameId(userId, boardGameId)) {
            userBoardGameRepository.deleteByUserIdAndBoardGameId(userId, boardGameId);
            return false;
        }
        // 좋아요
        else {
            UserBoardGame userBoardGame = new UserBoardGame();

            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            BoardGame boardGame = boardGameRepository.findById(boardGameId).orElseThrow(() -> new RuntimeException("BoardGame not found"));

            userBoardGame.setUser(user);
            userBoardGame.setBoardGame(boardGame);

            userBoardGameRepository.save(userBoardGame);
            return true;
        }
    }

}

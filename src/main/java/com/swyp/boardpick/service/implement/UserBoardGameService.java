package com.swyp.boardpick.service.implement;

import com.swyp.boardpick.domain.BoardGame;
import com.swyp.boardpick.domain.User;
import com.swyp.boardpick.domain.UserBoardGame;
import com.swyp.boardpick.repository.BoardGameRepository;
import com.swyp.boardpick.repository.UserBoardGameRepository;
import com.swyp.boardpick.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserBoardGameService {

    private final UserBoardGameRepository userBoardGameRepository;
    private final UserRepository userRepository;
    private final BoardGameRepository boardGameRepository;


    @Transactional
    public Boolean togglePick(Long userId, Long boardGameId) {

        Optional<BoardGame> optionalBoardGame = boardGameRepository.findById(boardGameId);
        if (optionalBoardGame.isEmpty())
            return false;

        BoardGame boardGame = optionalBoardGame.get();
        // 좋아요 취소
        if (userBoardGameRepository.existsByUserIdAndBoardGameId(userId, boardGameId)) {
            userBoardGameRepository.deleteByUserIdAndBoardGameId(userId, boardGameId);
            boardGame.decreaseLikes();
            boardGameRepository.save(boardGame);
            evictRecommendationCache(userId);
            return false;
        }
        // 좋아요
        else {
            UserBoardGame userBoardGame = new UserBoardGame();

            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

            userBoardGame.setUser(user);
            userBoardGame.setBoardGame(boardGame);
            userBoardGame.setDate(new Date());

            userBoardGameRepository.save(userBoardGame);
            boardGame.increaseLikes();
            boardGameRepository.save(boardGame);
            evictRecommendationCache(userId);
            return true;
        }
    }

    @CacheEvict(value = "recommendation", key = "#userId")
    private void evictRecommendationCache(Long userId) {
        // This method will evict the cache for the given user ID
    }

    public List<BoardGame> getMyPickList(Long userId) {
        return userBoardGameRepository
                .findByUserIdOrderByDateDesc(userId)
                .stream().map(UserBoardGame::getBoardGame)
                .toList();
    }

    public boolean getPicked(Long userId, Long boardGameId) {
        return userBoardGameRepository.existsByUserIdAndBoardGameId(userId, boardGameId);
    }

    public List<Long> getMyPickIds(Long userId) {
        return getMyPickList(userId)
                .stream().map(BoardGame::getId)
                .toList();
    }

}

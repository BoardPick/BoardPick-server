package com.swyp.boardpick.repository;

import com.swyp.boardpick.domain.UserBoardGame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBoardGameRepository extends JpaRepository<UserBoardGame, Long> {
    boolean existsByUserIdAndBoardGameId(Long userId, Long boardGameId);
    void deleteByUserIdAndBoardGameId(Long userId, Long boardGameId);
}

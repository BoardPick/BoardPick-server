package com.swyp.boardpick.repository;

import com.swyp.boardpick.domain.UserBoardGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserBoardGameRepository extends JpaRepository<UserBoardGame, Long> {
    boolean existsByUserIdAndBoardGameId(Long userId, Long boardGameId);
    void deleteByUserIdAndBoardGameId(Long userId, Long boardGameId);
    List<UserBoardGame> findByUserIdOrderByDateDesc(Long userId);

    @Query(value = "SELECT * FROM user_board_game ubg ORDER BY (SELECT COUNT(*) FROM user_board_game WHERE board_game_id = ubg.board_game_id) DESC", nativeQuery = true)
    List<UserBoardGame> findUserBoardGamesOrderByBoardGameIdCount();
}

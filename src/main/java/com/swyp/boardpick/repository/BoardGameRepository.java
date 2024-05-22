package com.swyp.boardpick.repository;

import com.swyp.boardpick.domain.BoardGame;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardGameRepository extends JpaRepository<BoardGame, Long> {

    List<BoardGame> findByNameContaining(String keyword, Pageable pageable);

    @Query("SELECT bg FROM BoardGame bg LEFT JOIN bg.userBoardGames ubg GROUP BY bg ORDER BY COUNT(ubg) DESC")
    Page<BoardGame> findByPickCountDesc(Pageable pageable);

    @Query("SELECT bg FROM BoardGame bg LEFT JOIN bg.userBoardGames ubg " +
            "WHERE DATE(ubg.date) = CURRENT_DATE " +
            "GROUP BY bg " +
            "ORDER BY COUNT(ubg) DESC")
    Page<BoardGame> findByPickCountDescForToday(Pageable pageable);
}

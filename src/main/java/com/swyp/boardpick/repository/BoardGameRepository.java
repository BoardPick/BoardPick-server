package com.swyp.boardpick.repository;

import com.swyp.boardpick.domain.BoardGame;
import com.swyp.boardpick.domain.BoardGameCategory;
import com.swyp.boardpick.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardGameRepository extends JpaRepository<BoardGame, Long> {
    Optional<BoardGame> findById(Long id);

    List<BoardGame> findByNameContaining(String keyword, Pageable pageable);

    @Query("SELECT bg FROM BoardGame bg LEFT JOIN bg.userBoardGames ubg GROUP BY bg ORDER BY COUNT(ubg) DESC")
    Page<BoardGame> findByPickCountDesc(Pageable pageable);

    @Query("SELECT bg FROM BoardGame bg LEFT JOIN bg.userBoardGames ubg " +
            "WHERE DATE(ubg.date) = CURRENT_DATE " +
            "GROUP BY bg " +
            "ORDER BY COUNT(ubg) DESC")
    Page<BoardGame> findByPickCountDescForToday(Pageable pageable);

    @Query("SELECT bg FROM BoardGame bg ORDER BY bg.difficulty")
    Page<BoardGame> findByPickDifficultyDesc(Pageable pageable);

    @Query("SELECT bg FROM BoardGame bg WHERE bg.maxPlayers=2 AND bg.minPlayers=2")
    Page<BoardGame> findByPick2PlayersDesc(Pageable pageable);

    @Query("SELECT bg FROM BoardGame bg ORDER BY bg.maxPlayers DESC")
    Page<BoardGame> findByPickPlayersDesc(Pageable pageable);

    @Query("SELECT bg FROM BoardGame bg JOIN bg.boardGameCategories bgc JOIN bgc.category c WHERE c IN :categories AND bg.id <> :boardGameId GROUP BY bg ORDER BY COUNT(c) DESC")
    List<BoardGame> findSimilarByCategories(@Param("categories") List<Category> categories, @Param("boardGameId") Long boardGameId);

    @Query(value = "SELECT * FROM board_game ORDER BY RAND()", nativeQuery = true)
    List<BoardGame> findRandomBoardGames(Pageable pageable);

    @Query("SELECT bg FROM BoardGame bg WHERE bg.id NOT IN :excludeIds ORDER BY RAND()")
    List<BoardGame> findRandomBoardGamesExcluding(@Param("excludeIds") List<Long> excludeIds, Pageable pageable);

}

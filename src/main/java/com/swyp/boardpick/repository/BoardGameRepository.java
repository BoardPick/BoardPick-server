package com.swyp.boardpick.repository;

import com.swyp.boardpick.domain.BoardGame;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardGameRepository extends JpaRepository<BoardGame, Long> {
    List<BoardGame> findByNameContaining(String keyword, Pageable pageable);
}

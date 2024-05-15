package com.swyp.boardpick.repository;

import com.swyp.boardpick.entity.BoardGame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardGameRepository extends JpaRepository<BoardGame, Long> {
}

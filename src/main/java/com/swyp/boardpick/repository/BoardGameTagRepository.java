package com.swyp.boardpick.repository;

import com.swyp.boardpick.domain.BoardGame;
import com.swyp.boardpick.domain.BoardGameTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardGameTagRepository extends JpaRepository<BoardGameTag, Long> {
    List<BoardGameTag> findBoardGameTagByBoardGame(BoardGame boardGame);
}

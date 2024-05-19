package com.swyp.boardpick.repository;

import com.swyp.boardpick.domain.BoardGameCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardGameCategoryRepository extends JpaRepository<BoardGameCategory, Long> {
    List<BoardGameCategory> findByCategory_Id(Long categoryId, Pageable pageable);
}

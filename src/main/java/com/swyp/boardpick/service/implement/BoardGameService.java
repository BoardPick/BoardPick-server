package com.swyp.boardpick.service.implement;

import com.swyp.boardpick.domain.BoardGame;
import com.swyp.boardpick.dto.response.BoardGameDto;
import com.swyp.boardpick.repository.BoardGameCategoryRepository;
import com.swyp.boardpick.repository.BoardGameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardGameService {
    private final BoardGameRepository boardGameRepository;
    private final BoardGameCategoryRepository boardGameCategoryRepository;

    public Optional<BoardGame> getBoardGameById(Long id) {
        return boardGameRepository.findById(id);
    }

    public List<BoardGameDto> getBoardGamesByCategory(String category, int page, int size) {
        return boardGameCategoryRepository.findByCategory(category, PageRequest.of(page, size))
                .stream().map(boardGameCategory -> {
                    BoardGame boardGame = boardGameRepository.findById(boardGameCategory.getId())
                            .orElseThrow(() -> new NoSuchElementException("찾는 보드게임이 없습니다."));
                    return new BoardGameDto(boardGame);
                }).toList();
    }
}

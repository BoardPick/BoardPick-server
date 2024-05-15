package com.swyp.boardpick.service.implement;

import com.swyp.boardpick.entity.BoardGame;
import com.swyp.boardpick.repository.BoardGameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardGameService {
    private final BoardGameRepository boardGameRepository;

    public Optional<BoardGame> getBoardGameById(Long id) {
        return boardGameRepository.findById(id);
    }
}

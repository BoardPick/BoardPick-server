package com.swyp.boardpick.service.implement;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.swyp.boardpick.domain.BoardGame;
import com.swyp.boardpick.repository.BoardGameRepository;
import com.swyp.boardpick.service.BoardGameRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimpleBoardGameRecommendationService implements BoardGameRecommendationService {

    private final BoardGameRepository boardGameRepository;

    @Override
    public List<BoardGame> recommendBoardGames() {
        List<BoardGame> allBoardGames = boardGameRepository.findAll();
        Collections.shuffle(allBoardGames);
        return allBoardGames.stream().limit(10).collect(Collectors.toList());
    }
}


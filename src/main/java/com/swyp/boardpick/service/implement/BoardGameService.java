package com.swyp.boardpick.service.implement;

import com.swyp.boardpick.domain.*;
import com.swyp.boardpick.dto.response.BoardGameDto;
import com.swyp.boardpick.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardGameService {
    private final BoardGameRepository boardGameRepository;
    private final BoardGameCategoryRepository boardGameCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final UserBoardGameService userBoardGameService;


    public BoardGame getBoardGameById(Long id) {
        return boardGameRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("BoardGame not found by id"));
    }

    public List<BoardGame> searchBoardGamesByKeyword(String keyword, int page, int size) {
        return boardGameRepository.findByNameContaining(keyword, PageRequest.of(page, size));
    }

    public List<BoardGame> recommendBoardGames() {
        List<BoardGame> allBoardGames = boardGameRepository.findAll();
        Collections.shuffle(allBoardGames);
        return allBoardGames.subList(0, Math.min(allBoardGames.size(), 10));
    }

    public List<BoardGame> getBoardGamesByCategory(String category, int page, int size) {
        Long categoryId = categoryRepository.findByType(category).getId();
        return boardGameCategoryRepository.findByCategory_Id(categoryId, PageRequest.of(page, size))
                .stream().map(BoardGameCategory::getBoardGame).toList();
    }

    public List<BoardGame> getBoardGamesByNumOfPick(int page, int size) {
        return boardGameRepository.findByPickCountDesc(PageRequest.of(page, size)).getContent();
    }

    public List<BoardGame> getTodayPick() {
        return boardGameRepository.findByPickCountDescForToday(PageRequest.of(0, 10)).getContent();
    }

    public List<BoardGame> getTop10(String filter) {
        Page<BoardGame> boardGames = boardGameRepository.findByPick2PlayersDesc(PageRequest.of(0, 10));
        if (filter.equals("difficulty")) {
            boardGames = boardGameRepository.findByPickDifficultyDesc(PageRequest.of(0, 10));
        } else if (filter.equals("players")) {
            boardGames = boardGameRepository.findByPickPlayersDesc(PageRequest.of(0, 10));
        }
        return boardGames.getContent();
    }

    public List<BoardGame> getSimilarBoardGamesById(Long id) {
        // 책임1 : 보드게임의 존재를 확인하고, 없다면 예외처리. 이 코드는 무조건 모듈화하는게 좋음
        BoardGame boardGame = boardGameRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("BoardGame not found"));

        List<Category> categories = boardGame.getBoardGameCategories()
                .stream()
                .map(BoardGameCategory::getCategory)
                .toList();

        return boardGameRepository.findSimilarByCategories(categories, id);
    }

    public List<BoardGameDto> convertToDtoList(List<BoardGame> boardGames, Long userId) {
        return boardGames
                .stream()
                .map(boardGame -> {
                    boolean picked = userBoardGameService.getPicked(userId, boardGame.getId());
                    return new BoardGameDto(boardGame, picked);
                }).toList();
    }

    public List<BoardGameDto> convertToDotListForAnonymous(List<BoardGame> boardGames) {
        return boardGames
                .stream()
                .map(boardGame -> {
                    return new BoardGameDto(boardGame, false);
                }).toList();
    }
}

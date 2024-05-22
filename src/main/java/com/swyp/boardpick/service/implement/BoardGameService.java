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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardGameService {
    private final BoardGameRepository boardGameRepository;
    private final BoardGameCategoryRepository boardGameCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final UserBoardGameRepository userBoardGameRepository;
    private final UserService userService;


    public Optional<BoardGameDto> getBoardGameById(Long id) {
        return boardGameRepository.findById(id)
                .map(this::convertToDto);
    }

    public List<BoardGameDto> searchBoardGamesByKeyword(String keyword, int page, int size) {
        return boardGameRepository.findByNameContaining(keyword, PageRequest.of(page, size))
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    public List<BoardGameDto> recommendBoardGames() {
        List<BoardGame> allBoardGames = boardGameRepository.findAll();
        Collections.shuffle(allBoardGames);
        return allBoardGames.stream()
                .limit(10)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<BoardGameDto> getBoardGamesByCategory(String category, int page, int size) {
        Long categoryId = categoryRepository.findByType(category).getId();
        return boardGameCategoryRepository.findByCategory_Id(categoryId, PageRequest.of(page, size))
                .stream().map(boardGameCategory -> {
                    BoardGame boardGame = boardGameCategory.getBoardGame();
                    List<String> tags = boardGame.getBoardGameTags()
                            .stream().map(boardGameTag -> boardGameTag.getTag().getContent())
                            .toList();
                    return new BoardGameDto(boardGame, tags);
                }).toList();
    }

    public List<BoardGameDto> getBoardGamesByNumOfPick(int page, int size) {
        return boardGameRepository.findByPickCountDesc(PageRequest.of(page, size))
                .map(boardGame -> {
                    List<String> tags = boardGame.getBoardGameTags()
                            .stream().map(boardGameTag -> boardGameTag.getTag().getContent())
                            .toList();
                    return new BoardGameDto(boardGame, tags);
                }).toList();
    }

    public List<BoardGameDto> getTodayPick() {
        return boardGameRepository.findByPickCountDescForToday(PageRequest.of(0, 10))
                .map(boardGame -> {
                    List<String> tags = boardGame.getBoardGameTags()
                            .stream().map(boardGameTag -> boardGameTag.getTag().getContent())
                            .toList();
                    return new BoardGameDto(boardGame, tags);
                }).toList();
    }

    public List<BoardGameDto> getTop10(String filter) {
        Page<BoardGame> boardGames = boardGameRepository.findByPick2PlayersDesc(PageRequest.of(0, 10));
        if (filter.equals("difficulty")) {
            boardGames = boardGameRepository.findByPickDifficultyDesc(PageRequest.of(0, 10));
        } else if (filter.equals("players")) {
            boardGames = boardGameRepository.findByPickPlayersDesc(PageRequest.of(0, 10));
        }
        return boardGames.map(boardGame -> convertToDto(boardGame)).stream().toList();
    }

    public List<BoardGameDto> getSimilarBoardGamesById(Long id) {
        BoardGame boardGame = boardGameRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("BoardGame not found"));

        List<Category> categories = boardGame.getBoardGameCategories()
                .stream()
                .map(BoardGameCategory::getCategory)
                .collect(Collectors.toList());

        return boardGameRepository.findSimilarByCategories(categories)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private BoardGameDto convertToDto(BoardGame boardGame) {
        String difficulty = convertDifficulty(boardGame.getDifficulty()).getDescription();

        List<String> boardGameCategories = boardGame.getBoardGameCategories()
                .stream().map(boardGameCategory -> boardGameCategory.getCategory().getType())
                .toList();

        List<String> tags =
                boardGame.getBoardGameTags()
                        .stream().map(boardGameTag -> boardGameTag.getTag().getContent())
                        .toList();

        Long userId = userService.getCurrentOAuth2UserId();

        Long boardGameId = boardGame.getId();

        boolean picked = userBoardGameRepository.existsByUserIdAndBoardGameId(userId, boardGameId);

        return new BoardGameDto(boardGame, difficulty, boardGameCategories, tags, picked);
    }

    private Difficulty convertDifficulty(double difficulty) {
        if (difficulty < 1.8)
            return Difficulty.VERY_EASY;
        if (difficulty < 2.6)
            return Difficulty.EASY;
        if (difficulty < 3.4)
            return Difficulty.NORMAL;
        if (difficulty < 4.2)
            return Difficulty.HARD;
        return Difficulty.VERY_HARD;
    }
}

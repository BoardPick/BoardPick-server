package com.swyp.boardpick.service.implement;

import com.swyp.boardpick.domain.*;
import com.swyp.boardpick.dto.response.BoardGameDto;
import com.swyp.boardpick.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardGameService {
    private final BoardGameRepository boardGameRepository;
    private final BoardGameCategoryRepository boardGameCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final UserBoardGameRepository userBoardGameRepository;
    private final UserService userService;


    public Optional<BoardGame> getBoardGameById(Long id) {
        return boardGameRepository.findById(id);
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

    public List<BoardGameDto> searchBoardGames(String keyword, int page, int size) {
        return boardGameRepository.findByNameContaining(keyword, PageRequest.of(page, size))
                .stream().map(boardGame -> {

                    List<String> tags =
                            boardGame.getBoardGameTags()
                            .stream().map(boardGameTag -> boardGameTag.getTag().getContent())
                            .toList();
                    Long userId = userService.getCurrentOAuth2UserId();
                    Long boardGameId = boardGame.getId();
                    boolean picked = userBoardGameRepository.existsByUserIdAndBoardGameId(userId, boardGameId);

                    return new BoardGameDto(boardGame, tags, picked);
                }).toList();
    }
}

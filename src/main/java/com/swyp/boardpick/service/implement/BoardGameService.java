package com.swyp.boardpick.service.implement;

import com.swyp.boardpick.domain.*;
import com.swyp.boardpick.dto.response.BoardGameDto;
import com.swyp.boardpick.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardGameService {
    private final BoardGameRepository boardGameRepository;
    private final BoardGameCategoryRepository boardGameCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final UserBoardGameService userBoardGameService;
    private final UserBoardGameRepository userBoardGameRepository;


    public BoardGame getBoardGameById(Long id) {
        return boardGameRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("BoardGame not found by id"));
    }

    public List<BoardGame> searchBoardGamesByKeyword(String keyword, int page, int size) {
        return boardGameRepository.findByNameContaining(keyword, PageRequest.of(page, size));
    }

    public List<BoardGame> fillRandomBoardGames(List<BoardGame> boardGames, Integer fullSize) {
        List<BoardGame> filledBoardGames = new ArrayList<>(boardGames);

        int size = fullSize - filledBoardGames.size();

        if (size > 0) {
            List<Long> existingIds = boardGames.stream()
                    .map(BoardGame::getId)
                    .toList();

            List<BoardGame> randomGames =
                    boardGameRepository.findRandomBoardGamesExcluding(existingIds, PageRequest.of(0, size));
            filledBoardGames.addAll(randomGames);
        }

        return filledBoardGames;
    }

    public List<BoardGame> getRandomBoardGames(Integer size) {
        return boardGameRepository.findRandomBoardGames(PageRequest.of(0, size));
    }

    public List<BoardGame> getSuggestionBoardGames(Long userId) {
        List<BoardGame> suggestionBoardGames = getPopularBoardGames();
        suggestionBoardGames.removeAll(getUserBoardGames(userId));

        return suggestionBoardGames;
    }

    public List<BoardGame> getPopularBoardGames() {
        List<BoardGame> popularBoardGames = userBoardGameRepository.findUserBoardGamesOrderByBoardGameIdCount()
                .stream().map(UserBoardGame::getBoardGame)
                .collect(Collectors.toList());

        Set<BoardGame> set = new LinkedHashSet<>(popularBoardGames);
        popularBoardGames.clear();
        popularBoardGames.addAll(set);

        return popularBoardGames;
    }

    public List<BoardGame> getSimilarBoardGamesById(Long id) {
        // 책임1 : 보드게임의 존재를 확인하고, 없다면 예외처리. 이 코드는 무조건 모듈화하는게 좋음
        BoardGame boardGame = boardGameRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("BoardGame not found"));

        List<BoardGame> allBoardGames = boardGameRepository.findAll()
                .stream()
                .filter(bg -> !bg.equals(boardGame))
                .toList();

        List<BoardGame> boardGames = Collections.singletonList(boardGame);

        Map<Category, Long> categoryScorecard = getCategoryScorecard(boardGames);
        Map<Tag, Long> tagScorecard = getTagScorecard(boardGames);

        List<BoardGame> similarBoardGames =
                scoringBoardGames(allBoardGames, categoryScorecard, tagScorecard)
                .stream().limit(10)
                .toList();

        return similarBoardGames;
    }

    @Cacheable("recommendation")
    public List<BoardGame> getRecommendationBoardGamesByUser(Long userId) {

        List<BoardGame> pickedGames = getUserBoardGames(userId);

        List<BoardGame> allBoardGames = boardGameRepository.findAll();
        allBoardGames.removeAll(pickedGames);

        List<BoardGame> recentlyPickedGames = pickedGames.stream().limit(20).toList();

        Map<Category, Long> categoryScorecard = getCategoryScorecard(recentlyPickedGames);
        Map<Tag, Long> tagScorecard = getTagScorecard(recentlyPickedGames);

        List<BoardGame> recommendedBoardGames =
                scoringBoardGames(allBoardGames, categoryScorecard, tagScorecard)
                .stream().limit(10)
                .toList();

        return recommendedBoardGames;
    }

    private Map<Category, Long> getCategoryScorecard(List<BoardGame> boardGames) {
        Map<Category, Long> scorecard = new HashMap<>();

        boardGames.forEach(boardGame -> {
            boardGame.getBoardGameCategories()
                    .stream()
                    .map(BoardGameCategory::getCategory)
                    .forEach(category -> scorecard.merge(category, 1L, Long::sum));
        });

        return scorecard;
    }

    private Map<Tag, Long> getTagScorecard(List<BoardGame> boardGames) {
        Map<Tag, Long> scorecard = new HashMap<>();

        boardGames.forEach(boardGame -> {
            boardGame.getBoardGameTags()
                    .stream()
                    .map(BoardGameTag::getTag)
                    .forEach(tag -> scorecard.merge(tag, 1L, Long::sum));
        });

        return scorecard;
    }

    private List<BoardGame> scoringBoardGames(List<BoardGame> boardGames, Map<Category, Long> categoryScorecard, Map<Tag, Long> tagScorecard) {
        Map<BoardGame, Long> boardGameScores = boardGames.stream()
                .collect(Collectors.toMap(
                        boardGame -> boardGame,
                        boardGame -> calculateScore(boardGame, categoryScorecard, tagScorecard)
                ));

        return boardGames.parallelStream()
                .sorted((bg1, bg2) -> Long.compare(boardGameScores.get(bg2), boardGameScores.get(bg1)))
                .toList();
    }

    private Long calculateScore(BoardGame boardGame, Map<Category, Long> categoryScorecard, Map<Tag, Long> tagScorecard) {
        Long score = 0L;

        List<Category> categories =
                boardGame.getBoardGameCategories()
                        .stream().map(BoardGameCategory::getCategory)
                        .toList();

        List<Tag> tags =
                boardGame.getBoardGameTags()
                        .stream().map(BoardGameTag::getTag)
                        .toList();

        for (Category category : categories)
            score += categoryScorecard.getOrDefault(category, 0L);

        for (Tag tag : tags)
            score += tagScorecard.getOrDefault(tag, 0L);

        return score;
    }

    public List<BoardGame> getUserBoardGames(Long userId) {
        return userBoardGameRepository.findByUserIdOrderByDateDesc(userId)
                .stream().map(UserBoardGame::getBoardGame)
                .toList();
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

    public List<BoardGameDto> convertToDtoList(List<BoardGame> boardGames, Long userId) {
        return boardGames
                .stream()
                .map(boardGame -> {
                    boolean picked = userBoardGameService.getPicked(userId, boardGame.getId());
                    return new BoardGameDto(boardGame, picked);
                }).toList();
    }

    public List<BoardGameDto> convertToDtoListForAnonymous(List<BoardGame> boardGames) {
        return boardGames
                .stream()
                .map(boardGame -> {
                    return new BoardGameDto(boardGame, false);
                }).toList();
    }
}

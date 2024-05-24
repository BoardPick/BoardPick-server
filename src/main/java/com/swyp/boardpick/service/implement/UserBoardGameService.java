package com.swyp.boardpick.service.implement;

import com.swyp.boardpick.domain.BoardGame;
import com.swyp.boardpick.domain.User;
import com.swyp.boardpick.domain.UserBoardGame;
import com.swyp.boardpick.dto.response.BoardGameDto;
import com.swyp.boardpick.repository.BoardGameRepository;
import com.swyp.boardpick.repository.UserBoardGameRepository;
import com.swyp.boardpick.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserBoardGameService {

    private final UserBoardGameRepository userBoardGameRepository;
    private final UserRepository userRepository;
    private final BoardGameRepository boardGameRepository;
    private final BoardGameService boardGameService;


    @Transactional
    public boolean togglePick(Long userId, Long boardGameId) {

        Optional<BoardGame> optionalBoardGame = boardGameRepository.findById(boardGameId);
        if (optionalBoardGame.isEmpty())
            return false;

        BoardGame boardGame = optionalBoardGame.get();
        // 좋아요 취소
        if (userBoardGameRepository.existsByUserIdAndBoardGameId(userId, boardGameId)) {
            userBoardGameRepository.deleteByUserIdAndBoardGameId(userId, boardGameId);
            boardGame.decreaseLikes();
            boardGameRepository.save(boardGame);
            return false;
        }
        // 좋아요
        else {
            UserBoardGame userBoardGame = new UserBoardGame();

            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

            userBoardGame.setUser(user);
            userBoardGame.setBoardGame(boardGame);
            userBoardGame.setDate(new Date());

            userBoardGameRepository.save(userBoardGame);
            boardGame.increaseLikes();
            boardGameRepository.save(boardGame);
            return true;
        }
    }

    public List<BoardGameDto> getMyPickList(Long id) {
        return userRepository.findById(id)
                .get().getUserBoardGames()
                .stream().map(userBoardGame -> {
                    BoardGame boardGame = userBoardGame.getBoardGame();
                    return boardGameService.convertToDto(boardGame);
                })
                .toList();
    }

}

package com.swyp.boardpick.utils;

import com.swyp.boardpick.domain.BoardGame;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BoardGameEntityListener {

    @PrePersist
    @PreUpdate
    public void beforeSave(BoardGame boardGame) {
        String formattedRuleUrl = formattingYoutubeUrl(boardGame.getRule());
        String formattedExtraUrl = formattingYoutubeUrl(boardGame.getExtraVideo());
        boardGame.setRule(formattedRuleUrl);
        boardGame.setExtraVideo(formattedExtraUrl);
    }

    public String formattingYoutubeUrl(String url) {
        String embedPrefix = "https://www.youtube.com/embed/";
        if (url.startsWith(embedPrefix))
            return url;

        String baseUrl = "https://www.youtube.com/embed/";
        return baseUrl + getUrlKey(url);
    }

    private String getUrlKey(String url) {
        String pattern = "(?<=youtu.be/|v=)[^&#?]*";

        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url);

        if (matcher.find())
            return matcher.group();

        return null;
    }
}

package com.example.Logitech.dto;

import com.example.Logitech.domain.Qna;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class QnaResponseDto {
    private final Long id;
    private final Integer level;
    private final Long parentId;
    private final String title;
    private final String content;
    private final String writer;
    private final int viewCount;
    private final LocalDateTime createdAt;
    private final boolean hasAnswer;

    public QnaResponseDto(Qna qna) {
        this(qna, false);
    }

    public QnaResponseDto(Qna qna, boolean hasAnswer) {
        this.id = qna.getId();
        this.level = qna.getLevel();
        this.parentId = qna.getParentId();
        this.title = qna.getTitle();
        this.content = qna.getContent();
        this.writer = qna.getWriter();
        this.viewCount = qna.getViewCount();
        this.createdAt = qna.getCreatedAt();
        this.hasAnswer = hasAnswer;
    }
}

package com.example.Logitech.service;

import com.example.Logitech.domain.Qna;
import com.example.Logitech.dto.QnaRequestDto;
import com.example.Logitech.dto.QnaResponseDto;
import com.example.Logitech.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QnaService {

    private final QnaRepository qnaRepository;

    public List<QnaResponseDto> getQnaList() {
        List<Qna> qnaList = qnaRepository.findAllGrouped();
        Set<Long> answeredParentIds = qnaList.stream()
                .filter(qna -> qna.getLevel() == 2)
                .map(Qna::getParentId)
                .collect(Collectors.toSet());

        return qnaList.stream()
                .map(qna -> new QnaResponseDto(qna, answeredParentIds.contains(qna.getId())))
                .toList();
    }

    public List<QnaResponseDto> findByTitle(String keyword) {
        return qnaRepository.findByTitle(keyword)
                .stream()
                .map(QnaResponseDto::new)
                .toList();
    }

    @Transactional
    public List<QnaResponseDto> detail(Long id) {
        Qna question = qnaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Q&A not found."));

        question.setViewCount(question.getViewCount() + 1);
        qnaRepository.save(question);

        return qnaRepository.findByParentIdOrderByLevelAscCreatedAtAsc(id)
                .stream()
                .map(QnaResponseDto::new)
                .toList();
    }

    @Transactional
    public QnaResponseDto addQuestion(QnaRequestDto request, String writer) {
        Qna qna = Qna.builder()
                .level(1)
                .title(request.getTitle())
                .content(request.getContent())
                .writer(writer)
                .build();

        Qna saved = qnaRepository.save(qna);
        saved.setParentId(saved.getId());

        return new QnaResponseDto(qnaRepository.save(saved));
    }

    @Transactional
    public QnaResponseDto addAnswer(Long parentId, QnaRequestDto request, String writer) {
        Qna qna = Qna.builder()
                .level(2)
                .parentId(parentId)
                .title(request.getTitle())
                .content(request.getContent())
                .writer(writer)
                .build();

        return new QnaResponseDto(qnaRepository.save(qna));
    }

    @Transactional
    public QnaResponseDto updateQna(Long id, QnaRequestDto request, String loginUser, boolean isAdmin) {
        Qna origin = qnaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Q&A not found."));

        if (!origin.getWriter().equals(loginUser) && !isAdmin) {
            throw new IllegalArgumentException("You do not have permission to update this Q&A.");
        }

        origin.setTitle(request.getTitle());
        origin.setContent(request.getContent());

        return new QnaResponseDto(qnaRepository.save(origin));
    }

    @Transactional
    public void delete(Long id, String loginUser, boolean isAdmin) {
        Qna origin = qnaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Q&A not found."));

        if (!origin.getWriter().equals(loginUser) && !isAdmin) {
            throw new IllegalArgumentException("You do not have permission to delete this Q&A.");
        }

        if (origin.getLevel() == 1) {
            List<Qna> qnaList = qnaRepository.findByParentIdOrderByLevelAscCreatedAtAsc(origin.getId());
            qnaRepository.deleteAll(qnaList);
            return;
        }

        qnaRepository.deleteById(id);
    }
}

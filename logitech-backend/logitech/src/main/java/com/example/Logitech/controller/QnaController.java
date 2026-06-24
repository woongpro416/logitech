package com.example.Logitech.controller;

import com.example.Logitech.dto.MemberResponseDto;
import com.example.Logitech.dto.QnaRequestDto;
import com.example.Logitech.dto.QnaResponseDto;
import com.example.Logitech.service.QnaService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/qna")
public class QnaController {

    private final QnaService qnaService;

    @GetMapping("/list")
    public List<QnaResponseDto> getQnaList() {
        return qnaService.getQnaList();
    }

    @GetMapping("/search")
    public List<QnaResponseDto> findByTitle(@RequestParam String keyword) {
        return qnaService.findByTitle(keyword);
    }

    @GetMapping("/detail/{id}")
    public List<QnaResponseDto> detailQna(@PathVariable Long id) {
        return qnaService.detail(id);
    }

    @PostMapping("/question")
    public QnaResponseDto addQuestion(@Valid @RequestBody QnaRequestDto qna, HttpSession session) {
        MemberResponseDto loginMember = getLoginMember(session);
        return qnaService.addQuestion(qna, loginMember.getName());
    }

    @PostMapping("/answer/{parentId}")
    public QnaResponseDto addAnswer(
            @PathVariable Long parentId,
            @Valid @RequestBody QnaRequestDto qna,
            HttpSession session
    ) {
        MemberResponseDto loginMember = getLoginMember(session);
        return qnaService.addAnswer(parentId, qna, loginMember.getName());
    }

    @PutMapping("/update/{id}")
    public QnaResponseDto update(@PathVariable Long id, @Valid @RequestBody QnaRequestDto qna, HttpSession session) {
        MemberResponseDto loginMember = getLoginMember(session);
        boolean isAdmin = loginMember.getRole().name().equals("ADMIN");
        return qnaService.updateQna(id, qna, loginMember.getName(), isAdmin);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteQna(@PathVariable Long id, HttpSession session) {
        MemberResponseDto loginMember = getLoginMember(session);
        boolean isAdmin = loginMember.getRole().name().equals("ADMIN");
        qnaService.delete(id, loginMember.getName(), isAdmin);
    }

    private MemberResponseDto getLoginMember(HttpSession session) {
        MemberResponseDto loginMember = (MemberResponseDto) session.getAttribute("loginMember");
        if (loginMember == null) {
            throw new IllegalArgumentException("Login is required.");
        }
        return loginMember;
    }
}

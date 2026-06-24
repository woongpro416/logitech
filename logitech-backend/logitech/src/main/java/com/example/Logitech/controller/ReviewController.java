package com.example.Logitech.controller;

import com.example.Logitech.dto.ReviewRequestDto;
import com.example.Logitech.dto.ReviewResponseDto;
import com.example.Logitech.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    public final ReviewService reviewService;

    //상품별 리뷰 목록
    @GetMapping("/item/{itemId}")
    public List<ReviewResponseDto> getReviewByItem(@PathVariable Long itemId) {
        return reviewService.getReviewByItem(itemId);
    }
    //회원별 리뷰 목록
    @GetMapping("/member/{memberId}")
    public List<ReviewResponseDto> getReviewByMember(@PathVariable Long memberId) {
        return reviewService.getReviewByMember(memberId);
    }

    //전체 리뷰 목록(관리자)
    @GetMapping("/list")
    public List<ReviewResponseDto> getAllReviews() {
        return reviewService.getAllReview();
    }

    //리뷰 등록
    @PostMapping("/new")
    public Long saveReview(@Valid @RequestBody ReviewRequestDto dto) {
        return reviewService.saveReview(dto);
    }

    //리뷰 수정
    @PutMapping("/edit/{reviewId}")
    public void updateReview(@PathVariable Long reviewId, @Valid @RequestBody ReviewRequestDto dto) {
        reviewService.updateReview(reviewId, dto);
    }

    //리뷰 삭제
    @DeleteMapping("/delete/{reviewId}")
    public void deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
    }
}

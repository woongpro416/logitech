package com.example.Logitech.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {

    @NotNull(message = "memberId is required.")
    private Long memberId;

    @NotNull(message = "itemId is required.")
    private Long itemId;

    @NotNull(message = "orderId is required.")
    private Long orderId;

    @Min(value = 1, message = "rating must be at least 1.")
    @Max(value = 5, message = "rating must be at most 5.")
    private int rating;

    @NotBlank(message = "content is required.")
    private String content;
}

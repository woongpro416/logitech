package com.example.Logitech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QnaRequestDto {

    @NotBlank(message = "title is required.")
    @Size(max = 255, message = "title must be 255 characters or fewer.")
    private String title;

    @NotBlank(message = "content is required.")
    private String content;
}

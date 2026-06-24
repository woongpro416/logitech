package com.example.Logitech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class OrderRequestDto {
    @NotNull(message = "memberId is required.")
    private Long memberId;

    @NotBlank(message = "address is required.")
    private String address;

    @NotBlank(message = "payment is required.")
    private String payment;

    @PositiveOrZero(message = "amount must be 0 or greater.")
    private int amount;

    @NotEmpty(message = "itemIds must not be empty.")
    private List<Long> itemIds;
}

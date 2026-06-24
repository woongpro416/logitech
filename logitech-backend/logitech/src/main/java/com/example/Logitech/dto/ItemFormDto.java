package com.example.Logitech.dto;

import com.example.Logitech.domain.ItemCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ItemFormDto {

    @NotBlank(message = "itemName is required.")
    private String itemName;

    @NotBlank(message = "imgPath is required.")
    private String imgPath;

    @PositiveOrZero(message = "price must be 0 or greater.")
    private int price;

    @PositiveOrZero(message = "stock must be 0 or greater.")
    private int stock;

    @NotNull(message = "category is required.")
    private ItemCategory category;
}

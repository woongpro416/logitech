package com.example.Logitech.controller;

import com.example.Logitech.dto.ItemFormDto;
import com.example.Logitech.dto.ItemResponseDto;
import com.example.Logitech.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    //상품 목록 조회
    @GetMapping("/list")
    public List<ItemResponseDto> list(@RequestParam(required = false) String itemName) {
        return itemService.getItemList(itemName);
    }

    //상품 상세보기
    @GetMapping("/detail/{itemId}")
    public ItemResponseDto getItem(@PathVariable Long itemId) {
        return itemService.getItem(itemId);
    }

    //상품 등록 폼
    @GetMapping("/new")
    public ItemFormDto newItemForm() {
        return new ItemFormDto();
    }

    //상품 등록 처리
    @PostMapping("/new")
    public Long saveItem(@Valid @RequestBody ItemFormDto dto) {
        return itemService.saveItem(dto);
    }

    //상품 수정 폼
    @GetMapping("/edit/{itemId}")
    public ItemResponseDto updateItemForm(@PathVariable Long itemId) {
        return itemService.getItem(itemId);
    }

    //상품 수정 처리
    @PutMapping("/edit/{itemId}")
    public void updateItem(@PathVariable Long itemId, @Valid @RequestBody ItemFormDto dto) {
        itemService.updateItem(itemId, dto);
    }

    //상품 삭제
    @DeleteMapping("/delete/{itemId}")
    public void deleteItem(@PathVariable Long itemId) {
        itemService.deleteItem(itemId);
    }

    //재고 수정(관리자)
    @PatchMapping("/stock/{itemId}")
    public void updateStock(@PathVariable Long itemId, @RequestParam int stock) {
        itemService.updateStock(itemId, stock);
    }
}

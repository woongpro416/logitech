package com.example.Logitech.controller;

import com.example.Logitech.domain.Member;
import com.example.Logitech.dto.OrderRequestDto;
import com.example.Logitech.dto.OrderResponseDto;
import com.example.Logitech.repository.MemberRepository;
import com.example.Logitech.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final MemberRepository memberRepository;


    //전체 주문 목록(관리자)
    @GetMapping("/list")
    public List<OrderResponseDto> getAllOrder() {
        return orderService.getAllOrders();
    }

    //주문 생성
    @PostMapping("/new")
    public Long createOrder(@Valid @RequestBody OrderRequestDto dto) {
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다."));
        return orderService.createOrder(member, dto);
    }

    //주문 목록 조회
    @GetMapping("/list/{memberId}")
    public List<OrderResponseDto> getOrderList(@PathVariable Long memberId) {
        return orderService.getOrderList(memberId);
    }

    //주문 상세 조회
    @GetMapping("/detail/{orderId}")
    public OrderResponseDto getOrder(@PathVariable Long orderId) {
        return orderService.getOrder(orderId);
    }

    //주문 취소
    @DeleteMapping("/delete/{orderId}")
    public void deleteOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
    }
}

package com.example.Logitech.controller;

import com.example.Logitech.domain.Member;
import com.example.Logitech.domain.Role;
import com.example.Logitech.dto.MemberJoinRequestDto;
import com.example.Logitech.dto.MemberLoginRequestDto;
import com.example.Logitech.dto.MemberResponseDto;
import com.example.Logitech.dto.MemberUpdateDto;
import com.example.Logitech.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")

public class MemberController {

    private final MemberService memberService;

    //회원가입 폼
    @GetMapping("/join")
    public MemberJoinRequestDto joinForm() {
        return new MemberJoinRequestDto();
    }

    //회원가입 처리
    @PostMapping("/join")
    public Long join(@Valid @RequestBody MemberJoinRequestDto dto) {
        return memberService.join(dto);
    }

    //로그인 폼
    @GetMapping("/login")
    public MemberLoginRequestDto loginForm() {
        return new MemberLoginRequestDto();
    }

    //로그아웃 처리
    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        request.getSession().invalidate();
    }

    //회원 목록 조회 ( 관리자용 )
    @GetMapping("/list")
    public List<MemberResponseDto> list() {
        return memberService.getMemberList();
    }

    //회원 상세보기
    @GetMapping("/detail/{memberID}")
    public MemberResponseDto getMember(@PathVariable Long memberID) {
        return memberService.getMember(memberID);
    }

    //회원 정보 변경 폼
    @GetMapping("/edit/{memberID}")
    public MemberResponseDto updateMemberForm(@PathVariable Long memberID) {
        return memberService.getMember(memberID);
    }

    //회원 정보 변경 처리
    @PutMapping("/edit/{memberID}")
    public void updateMember(@PathVariable Long memberID, @Valid @RequestBody MemberUpdateDto request) {
        memberService.updateMember(memberID, request);
    }

    //회원 탈퇴
    @DeleteMapping("/delete/{memberID}")
    public void deleteMemer(@PathVariable Long memberID) {
        memberService.deleteMember(memberID);
    }

    //로그인 상태 확인
    @GetMapping("/check")
    public MemberResponseDto check(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        return (MemberResponseDto) session.getAttribute("loginMember");
    }

    // 아이디 중복확인
    @GetMapping("/check-id")
    public boolean checkLoginId(@RequestParam String loginId) {
        return memberService.checkLoginId(loginId);
    }

    //로그인 확인
    @PostMapping("/login")
    public MemberResponseDto login(@Valid @RequestBody MemberLoginRequestDto request,
                                   HttpServletRequest httpRequest) {
        MemberResponseDto response = memberService.login(request);

        HttpSession oldSession = httpRequest.getSession(false);
        if (oldSession != null) oldSession.invalidate();

        Role role = response.getRole() != null ? response.getRole() : Role.USER;
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        response.getLoginId(),
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + role.name()))
                );
        SecurityContextHolder.getContext().setAuthentication(auth);

        HttpSession session = httpRequest.getSession(true);
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext()
        );
        session.setAttribute("loginMember", response);

        return response;
    }

    //회원 검색
    @GetMapping("/search")
    public List<MemberResponseDto> searchMember(@RequestParam String keyword) {
        return memberService.searchMember(keyword);
    }

    //회원 등급 변경
    @PatchMapping("/role/{memberId}")
    public void changeRole(@PathVariable Long memberId, @RequestParam Role role) {
        memberService.changeRole(memberId, role);
    }

    //총 회원수 조회
    @GetMapping("/count")
    public long getMemberCount() {
        return memberService.getMemberCount();
    }

}

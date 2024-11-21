package exam.myBank.controller;

import exam.myBank.domain.dto.memberDto.MemberResponseDto;
import exam.myBank.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{memberId}")
    public MemberResponseDto findMember(@PathVariable("memberId") Long memberId) {
        return memberService.findByMemberId(memberId);
    }

    @GetMapping
    public List<MemberResponseDto>findAll() {
        return memberService.findMembers();
    }

    @DeleteMapping("/{memberId}")
    public void deleteMember(@PathVariable("memberId") Long memberId) {
        memberService.deleteMember(memberId);
    }

    @DeleteMapping
    public void deleteAll() {
        memberService.clear();
    }
}

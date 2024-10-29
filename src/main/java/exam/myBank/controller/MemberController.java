package exam.myBank.controller;

import exam.myBank.domain.dto.memberDto.JoinRequestDto;
import exam.myBank.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody JoinRequestDto requestDto) {
        log.info("dto username : {}", requestDto.getUsername());
        memberService.join(requestDto.getUsername(), requestDto.getPassword());
        return ResponseEntity.ok().body("회원가입 성공");
    }
}

package exam.myBank.controller;

import exam.myBank.domain.dto.memberDto.JoinRequestDto;
import exam.myBank.domain.dto.memberDto.LoginRequestDto;
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


}

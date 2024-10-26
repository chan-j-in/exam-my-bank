package exam.myBank.controller;

import exam.myBank.dto.ResponseDto;
import exam.myBank.dto.memberDto.SignupRequestDto;
import exam.myBank.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    public ResponseDto<String> signup(@RequestBody SignupRequestDto requestDto) {

        if (!requestDto.getPassword().equals(requestDto.getPassword2())) {
            return new ResponseDto<>(false, "두 비밀번호가 일치하지 않습니다.", null);
        }

        return memberService.join(requestDto.getUsername(), requestDto.getEmail(), requestDto.getPassword());
    }
}

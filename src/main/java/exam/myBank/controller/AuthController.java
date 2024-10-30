package exam.myBank.controller;

import exam.myBank.domain.dto.memberDto.JoinRequestDto;
import exam.myBank.domain.dto.memberDto.LoginRequestDto;
import exam.myBank.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody JoinRequestDto requestDto) {

        log.info("dto username : {}", requestDto.getUsername());

        String username = authService.join(requestDto.getUsername(), requestDto.getPassword());
        return ResponseEntity.ok().body("회원가입 성공 id : " + username);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto requestDto) {

        String token = authService.login(requestDto.getUsername(), requestDto.getPassword());
        return ResponseEntity.ok().body(token);
    }
}

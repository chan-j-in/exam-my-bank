package exam.myBank.controller;

import exam.myBank.domain.dto.accountDto.AccountCreateRequestDto;
import exam.myBank.domain.dto.accountDto.AccountResponseDto;
import exam.myBank.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public List<AccountResponseDto> findAccounts() {

        return accountService.findAll();
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody AccountCreateRequestDto requestDto) {
        return accountService.create(requestDto);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test(Authentication authentication) {

        return ResponseEntity.ok().body(authentication.getName()+" hello!");
    }
}

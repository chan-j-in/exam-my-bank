package exam.myBank.controller;

import exam.myBank.domain.dto.accountDto.*;
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

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody TransactionRequestDto requestDto) {
        return accountService.deposit(requestDto);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody TransactionRequestDto requestDto) {
        return accountService.withdraw(requestDto);
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransferRequestDto requestDto) {
        return accountService.transfer(requestDto);
    }

    @GetMapping("/{accountNum}/transactions")
    public List<TransactionResponseDto> getTransactions(@PathVariable("accountNum") String accountNum) {
        return accountService.getTransactionHistory(accountNum);
    }
}

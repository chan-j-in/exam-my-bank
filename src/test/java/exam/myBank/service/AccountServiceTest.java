package exam.myBank.service;

import exam.myBank.domain.entity.Account;
import exam.myBank.domain.entity.Member;
import exam.myBank.domain.repository.AccountRepository;
import exam.myBank.dto.ResponseDto;
import exam.myBank.type.Bank;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class AccountServiceTest {

    @Autowired
    private AccountService accountService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private AccountRepository accountRepository;

    @Test
    void 계좌생성() throws Exception {
        //given
        Member member = memberService.join("userA", "abc@naver.com","1234");
        Account account = accountService.create("accountA", member.getId(), Bank.B_BANK);

        //when
        Account findAccount = accountRepository.findByAccountNum(account.getAccountNum()).get();

        //then
        assertThat(findAccount.getAccountNum()).isEqualTo(account.getAccountNum());
        assertThat(findAccount.getAccountName()).isEqualTo(account.getAccountName());

    }
}
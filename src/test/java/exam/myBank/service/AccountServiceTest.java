package exam.myBank.service;

import exam.myBank.domain.entity.Account;
import exam.myBank.domain.entity.Member;
import exam.myBank.domain.repository.AccountRepository;
import exam.myBank.type.Bank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class AccountServiceTest {

    @Autowired
    private AccountService accountService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        accountService.clear();
        memberService.clear();
    }

    @Test
    @WithMockUser(username = "userA", roles = "USER")
    void 계좌생성() throws Exception {
        //given
        Member member = memberService.join("userA", "abc@naver.com","1234");
        Account account = accountService.create("accountA", Bank.B_BANK);

        //when
        Account findAccount = accountRepository.findByAccountNum(account.getAccountNum()).get();

        //then
        assertThat(findAccount.getAccountNum()).isEqualTo(account.getAccountNum());
        assertThat(findAccount.getAccountName()).isEqualTo(account.getAccountName());

    }

    @Test
    @WithMockUser(username = "userB", roles = "USER")
    void 계좌중복생성() throws Exception {
        // given
        Member member = memberService.join("userB", "abc@naver.com","1234");
        accountService.create("accountB", Bank.B_BANK);

        // when & then
        assertThatThrownBy(() -> accountService.create("accountB", Bank.B_BANK))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("중복된 계좌 이름입니다.");
    }

    @Test
    @WithMockUser(username = "userC", roles = "USER")
    void 자신의계좌조회() throws Exception {
        // given
        Member member = memberService.join("userC", "abc@naver.com", "1234");
        accountService.create("accountC", Bank.A_BANK);

        // when
        List<Account> accounts = accountService.findAccounts();

        // then
        assertThat(accounts).hasSize(1);
        assertThat(accounts.get(0).getAccountName()).isEqualTo("accountC");
    }

    @Test
    @WithMockUser(username = "userD", roles = "USER")
    void 다른_사용자의_계좌조회() throws Exception {
        // given
        Member memberA = memberService.join("userD", "abc@naver.com", "1234");
        Member memberB = memberService.join("userE", "def@naver.com", "5678");

        accountService.create("accountD", Bank.B_BANK);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(memberB.getUsername(), "5678",
                Collections.singletonList(new SimpleGrantedAuthority("USER"))));

        // when
        List<Account> accountsForMemberB = accountService.findAccounts();

        // then
        assertThat(accountsForMemberB).isEmpty();
    }

    @Test
    @WithMockUser(username = "userF", roles = "USER")
    void 계좌삭제() throws Exception {
        // given
        Member member = memberService.join("userF", "abc@naver.com", "1234");
        Account account = accountService.create("accountE", Bank.A_BANK);

        // when
        accountService.delete(account.getId());

        // then
        assertThat(accountRepository.findById(account.getId())).isEmpty();
    }

    @Test
    void 인증되지_않은_사용자_계좌생성() throws Exception {
        // given
        SecurityContextHolder.clearContext();

        // when & then
        assertThatThrownBy(() -> accountService.create("accountF", Bank.A_BANK))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("현재 사용자 정보를 찾을 수 없습니다.");
    }
}
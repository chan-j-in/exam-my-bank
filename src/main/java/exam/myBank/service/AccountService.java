package exam.myBank.service;

import exam.myBank.domain.entity.Account;
import exam.myBank.domain.entity.Member;
import exam.myBank.domain.repository.AccountRepository;
import exam.myBank.domain.repository.MemberRepository;
import exam.myBank.type.Bank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@PreAuthorize("isAuthenticated()")
public class AccountService {

    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Account create(String accountName, Bank bank) {

        if (accountRepository.existsByAccountName(accountName)) throw new IllegalArgumentException("중복된 계좌 이름입니다.");

        Member member = getCurrentMember();

        Account account = Account.builder()
                .accountName(accountName)
                .accountNum(makeAccountNum(bank))
                .member(member)
                .bank(bank)
                .build();
        accountRepository.save(account);
        return account;
    }

    public List<Account> findAccounts() {

        Member member = getCurrentMember();
        return accountRepository.findByMemberId(member.getId());
    }

    @Transactional
    public Long deposit(String accountNum, Long amount) {

        Account account = getAccountIfOwnedByCurrentUser(accountNum);
        return account.deposit(amount);
    }

    @Transactional
    public Long withdraw(String accountNum, Long amount) {

        Account account = getAccountIfOwnedByCurrentUser(accountNum);
        return account.withdraw(amount);
    }

    @Transactional
    public void delete(String accountNum) {

        Account account = getAccountIfOwnedByCurrentUser(accountNum);
        accountRepository.delete(account);
    }

    private String makeAccountNum(Bank bank) {

        String bankNum = switch (bank) {
            case A_BANK -> "123";
            case B_BANK -> "456";
            case C_BANK -> "789";
            default -> "000";
        };

        String accountNum;
        do {
            accountNum = bankNum + UUID.randomUUID().toString().substring(0, 8);
        } while (accountRepository.findByAccountNum(accountNum).isPresent());

        return accountNum;
    }

    private Member getCurrentMember() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("현재 사용자 정보를 찾을 수 없습니다."); // 예외 발생
        }

        return memberRepository.findByUsername(authentication.getName()).orElseThrow(
                ()-> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    private Account getAccountIfOwnedByCurrentUser(String accountNum) {
        Account account = accountRepository.findByAccountNum(accountNum)
                .orElseThrow(() -> new IllegalArgumentException("해당 계좌를 찾을 수 없습니다."));

        Member currentMember = getCurrentMember();
        if (!account.getMember().getId().equals(currentMember.getId())) {
            throw new IllegalArgumentException("해당 계좌에 대한 접근 권한이 없습니다.");
        }

        return account;
    }

    @Transactional
    public void clear() {
        accountRepository.deleteAll();
    }
}

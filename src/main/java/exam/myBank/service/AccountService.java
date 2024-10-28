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
    public void delete(Long accountId) {
        accountRepository.deleteById(accountId);
    }

    private String makeAccountNum(Bank bank) {

        String bankNum;
        if (bank == Bank.A_BANK)
            bankNum = "123";
        else if (bank == Bank.B_BANK)
            bankNum = "456";
        else
            bankNum = "789";

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

    @Transactional
    public void clear() {
        accountRepository.deleteAll();
    }
}

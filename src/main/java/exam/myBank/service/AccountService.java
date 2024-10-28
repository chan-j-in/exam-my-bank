package exam.myBank.service;

import exam.myBank.domain.entity.Account;
import exam.myBank.domain.entity.Member;
import exam.myBank.domain.repository.AccountRepository;
import exam.myBank.domain.repository.MemberRepository;
import exam.myBank.dto.ResponseDto;
import exam.myBank.type.Bank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Account create(String accountName, Long memberId, Bank bank) {

        Optional<Member> member = memberRepository.findById(memberId);
//        if (member.isEmpty()) return new ResponseDto<>(false, "회원을 찾을 수 없습니다.", null);
//
//        if (accountRepository.findByAccountName(accountName).get().getMember().getId()==memberId) {
//            return new ResponseDto<>(false, "중복된 이름의 계좌가 존재합니다.", null);
//        }

        Account account = Account.builder()
                .accountName(accountName)
                .accountNum(makeAccountNum(bank))
                .member(member.get())
                .bank(bank)
                .build();
        accountRepository.save(account);
        return account;
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
}

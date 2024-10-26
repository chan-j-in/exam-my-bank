package exam.myBank.service;

import exam.myBank.domain.entity.Member;
import exam.myBank.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member join(String username, String email, String password) {
        Member member = Member.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();
        memberRepository.save(member);

        return member;
    }

}

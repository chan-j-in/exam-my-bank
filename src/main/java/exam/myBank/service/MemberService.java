package exam.myBank.service;

import exam.myBank.domain.entity.Member;
import exam.myBank.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member join(String username, String email, String password) {

        try {
            Member member = Member.builder()
                    .username(username)
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .build();
            memberRepository.save(member);
            return member;
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("중복된 이메일이나 회원명입니다.");
        }
    }

    private boolean emailValidation(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isEmpty()) return true;
        return false;
    }

    private boolean usernameValidation(String username) {
        Optional<Member> member = memberRepository.findByUsername(username);
        if (member.isEmpty()) return true;
        return false;
    }

}

package exam.myBank.service;

import exam.myBank.domain.entity.Member;
import exam.myBank.domain.repository.MemberRepository;
import exam.myBank.dto.ResponseDto;
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
    public ResponseDto<String> join(String username, String email, String password) {

        if (usernameValidation(username)) return new ResponseDto<>(false, "중복된 사용자명입니다.", username);
        if (emailValidation(email)) return new ResponseDto<>(false, "중복된 이메일입니다.", email);

        Member member = Member.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();
        memberRepository.save(member);
        return new ResponseDto<>(true, "회원가입 성공", username);

    }

    private boolean emailValidation(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

    private boolean usernameValidation(String username) {
        return memberRepository.findByUsername(username).isPresent();
    }

}

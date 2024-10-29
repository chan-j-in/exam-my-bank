package exam.myBank.service;

import exam.myBank.domain.entity.Member;
import exam.myBank.domain.repository.MemberRepository;
import exam.myBank.dto.memberDto.SignInRequestDto;
import exam.myBank.dto.memberDto.SignupRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public String signIn(SignInRequestDto requestDto) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword()
                    )
            );

            return "로그인 성공 id: "+requestDto.getUsername();
        } catch (AuthenticationException e) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 잘못되었습니다.");
        }
    }

    @Transactional
    public Member join(SignupRequestDto requestDto) {

        if (usernameValidation(requestDto.getUsername())) throw new IllegalArgumentException("이미 사용 중인 사용자 이름입니다.");
        if (emailValidation(requestDto.getUsername())) throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        if (!requestDto.getPassword().equals(requestDto.getPasswordCheck())) throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");

        Member member = Member.builder()
                .username(requestDto.getUsername())
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .build();
        return memberRepository.save(member);
    }

    private boolean emailValidation(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

    private boolean usernameValidation(String username) {
        return memberRepository.findByUsername(username).isPresent();
    }

}

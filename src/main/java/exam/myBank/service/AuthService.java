package exam.myBank.service;

import exam.myBank.dto.JoinRequestDto;
import exam.myBank.domain.entity.Member;
import exam.myBank.domain.repository.MemberRepository;
import exam.myBank.dto.LoginRequestDto;
import exam.myBank.exception.AppException;
import exam.myBank.exception.ErrorCode;
import exam.myBank.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AuthService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secretKey;

    private Long expiredMs = 1000 * 60L * 60;

    @Transactional
    public String join(JoinRequestDto requestDto) {

        String username = requestDto.getUsername();
        String password = requestDto.getPassword();
        String passwordCheck = requestDto.getPasswordCheck();

        log.info("username : {}",username);
        log.info("password : {}",password);

        memberRepository.findByUsername(username).ifPresent(user -> {
            throw new AppException(ErrorCode.USERNAME_DUPLICATED, username+"은(는) 이미 있습니다.");
        });

        if (!password.equals(passwordCheck)) throw new AppException(ErrorCode.INVALID_PASSWORD, "비밀번호가 일치하지 않습니다.");

        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();
        memberRepository.save(member);

        return member.getUsername();
    }

    public String login(LoginRequestDto requestDto) {

        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, username + "을(를) 찾을 수 없습니다."));

        if (!passwordEncoder.matches(password,member.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD, "비밀번호를 확인해주세요.");
        }

        return JwtUtil.createJWT(username, secretKey, expiredMs);
    }
}

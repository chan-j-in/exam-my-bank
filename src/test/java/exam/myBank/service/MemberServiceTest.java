package exam.myBank.service;

import exam.myBank.domain.entity.Member;
import exam.myBank.domain.repository.MemberRepository;
import exam.myBank.dto.memberDto.SignupRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private AuthService authService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        memberService.clear();
    }

    @Test
    void 회원가입() throws Exception {
        //given
        Member member = authService.join(new SignupRequestDto("userA", "abc@abc.com", "1234", "1234"));

        //when
        Optional<Member> findMember = memberRepository.findByUsername("userA");

        //then
        assertThat(member.getEmail()).isEqualTo(findMember.get().getEmail());
        assertThat(member.getPassword()).isEqualTo(findMember.get().getPassword());
        System.out.println(member.getPassword());
    }

    @Test
    void 중복회원가입_예외처리() throws Exception {
        // given
        authService.join(new SignupRequestDto("userA", "abc@abc.com", "1234", "1234"));

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.join(new SignupRequestDto("userA", "abc@abc.com", "1234", "1234"));
        });

        // 예외 메시지 검증
        assertEquals("이미 사용 중인 사용자 이름입니다.", exception.getMessage());
    }

}
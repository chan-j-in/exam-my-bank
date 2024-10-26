package exam.myBank.service;

import exam.myBank.domain.entity.Member;
import exam.myBank.domain.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void 회원가입() throws Exception {
        //given
        Member member = Member.builder()
                .username("kim")
                .email("abc@abc.com")
                .password(passwordEncoder.encode("1234"))
                .build();
        memberService.join(member.getUsername(), member.getEmail(), member.getPassword());

        //when
        Optional<Member> findMember = memberRepository.findByUsername("kim");

        //then
        assertThat(member.getEmail()).isEqualTo(findMember.get().getEmail());
        assertThat(member.getPassword()).isEqualTo(findMember.get().getPassword());
        System.out.println(member.getPassword());
    }

    @Test
    void 중복회원가입() throws Exception {
        //given
        Member memberA = Member.builder()
                .username("kim")
                .email("abc@abc.com")
                .password(passwordEncoder.encode("1234"))
                .build();

        //when
        memberService.join(memberA.getUsername(), memberA.getEmail(), memberA.getPassword());

        //then
        assertThrows(IllegalArgumentException.class, () -> memberService.join(memberA.getUsername(), memberA.getEmail(), memberA.getPassword()));

    }

}
package exam.myBank.service;

import exam.myBank.domain.entity.Member;
import exam.myBank.domain.repository.MemberRepository;
import exam.myBank.exception.AppException;
import exam.myBank.exception.ErrorCode;
import exam.myBank.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public Member findMemberById(Long memberId) {

        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
    }

    public List<Member> findMembers() {

        return memberRepository.findAll();
    }


//    @Transactional
//    public Member updatePassword(String password) {
//
//        Member member = getCurrentMember();
//        member.updatePassword(password);
//        return member;
//    }
//
//    @Transactional
//    public void delete() {
//
//        Member member = getCurrentMember();
//        memberRepository.delete(member);
//    }


    @Transactional
    public void clear() {
        memberRepository.deleteAll();
    }
}

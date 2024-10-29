package exam.myBank.service;

import exam.myBank.domain.entity.Member;
import exam.myBank.domain.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
public class MemberService extends BaseService {

    public MemberService(MemberRepository memberRepository) {
        super(memberRepository);
    }

    public Member findMemberById(Long memberId) {

        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
    }

    public List<Member> findMembers() {

        return memberRepository.findAll();
    }

    @Transactional
    public Member updateEmail(String email) {

        Member member = getCurrentMember();
        member.updateEmail(email);
        return memberRepository.save(member);
    }

    @Transactional
    public Member updatePassword(String password) {

        Member member = getCurrentMember();
        member.updatePassword(password);
        return memberRepository.save(member);
    }

    @Transactional
    public void delete() {

        Member member = getCurrentMember();
        memberRepository.delete(member);
    }

    @Transactional
    public void clear() {
        memberRepository.deleteAll();
    }

}

package exam.myBank.service;

import exam.myBank.domain.entity.Member;
import exam.myBank.domain.repository.MemberRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class BaseService {

    protected final MemberRepository memberRepository;

    protected BaseService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    protected Member getCurrentMember() {
        // 현재 로그인된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        // 현재 사용자 이름으로 회원 정보 조회
        return memberRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new IllegalArgumentException("현재 로그인된 회원이 존재하지 않습니다."));
    }
}
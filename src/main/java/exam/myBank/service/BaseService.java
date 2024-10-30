package exam.myBank.service;

import exam.myBank.domain.entity.Member;
import exam.myBank.domain.repository.MemberRepository;
import exam.myBank.exception.AppException;
import exam.myBank.exception.ErrorCode;
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

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.USER_NOT_AUTHENTICATED, "사용자가 인증되지 않았습니다.");
        }

        String currentUsername = authentication.getName();

        // 현재 사용자 이름으로 회원 정보 조회
        return memberRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }
}
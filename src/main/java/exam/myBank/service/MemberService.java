package exam.myBank.service;

import exam.myBank.domain.dto.memberDto.MemberResponseDto;
import exam.myBank.domain.entity.Member;
import exam.myBank.domain.repository.MemberRepository;
import exam.myBank.exception.AppException;
import exam.myBank.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberResponseDto findByMemberId(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        return toResponseDto(member);
    }

    public List<MemberResponseDto> findMembers() {

        List<Member> members = memberRepository.findAll();

        return members.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteMember(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다."));
        memberRepository.delete(member);
    }

    @Transactional
    public void clear() {
        memberRepository.deleteAll();
    }

    private MemberResponseDto toResponseDto(Member member) {
        return new MemberResponseDto(member.getUsername(), member.getAccounts());
    }

}

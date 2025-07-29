package tobyspring.splearn.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import tobyspring.splearn.application.provided.MemberFinder;
import tobyspring.splearn.application.required.MemberRepository;
import tobyspring.splearn.domain.Member;

import java.util.function.Supplier;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class MemberQueryService implements MemberFinder {

    private final MemberRepository memberRepository;


    @Override
    public Member find(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(IllegalArgument(String.valueOf(memberId)));
    }

    private Supplier<IllegalArgumentException> IllegalArgument(String message) {
        return ()-> new IllegalArgumentException("회원을 찾을 수 업습니다. id: " + message);
    }
}

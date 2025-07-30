package tobyspring.splearn.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tobyspring.splearn.application.provided.MemberRegister;
import tobyspring.splearn.application.required.EmailSender;
import tobyspring.splearn.application.required.MemberRepository;
import tobyspring.splearn.domain.*;

@Service
@RequiredArgsConstructor
public class MemberService implements MemberRegister {

    private final MemberRepository memberRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member register(MemberRegisterRequest registerRequest) {
        // check
        checkDuplicateEmail(registerRequest);

        // domain model
        Member member = Member.register(registerRequest, passwordEncoder);

        // repository
        memberRepository.save(member);

        // post process
        sendWelcomeEmail(member);

        return member;
    }

    private void sendWelcomeEmail(Member member) {
        emailSender.send(member.getEmail(), "등록을 완료해주세요.", "아래 링크를 클랙해서 등록을 완료해주세요");
    }

    private void checkDuplicateEmail(MemberRegisterRequest registerRequest) {
        if(memberRepository.findByEmail(new Email(registerRequest.email())).isPresent()){
            throw new DuplicateEmailException("등로된 이메일 입니다" + registerRequest.email());
        }
    }
}

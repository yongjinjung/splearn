package tobyspring.splearn.application.member.provided;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import tobyspring.splearn.application.member.MemberModifyService;
import tobyspring.splearn.application.member.MemberQueryService;
import tobyspring.splearn.application.member.required.EmailSender;
import tobyspring.splearn.application.member.required.MemberRepository;
import tobyspring.splearn.domain.shared.Email;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.member.MemberFixture;
import tobyspring.splearn.domain.member.MemberStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class MemberRegisterManualTest {

    @Test
    @DisplayName("회원 생성 Stub")
    void registerTestStub() {
        MemberRegister register = new MemberModifyService(
                new MemberQueryService(new MemberRepositoryStub()), new MemberRepositoryStub(), new EmailSenderStub(), MemberFixture.createPasswordEncoder()
        );

        Member member = register.register(MemberFixture.createMemberRegisterReques());
        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

    }

    @Test
    @DisplayName("회원 생성 Mock")
    void registerTestMock() {
        EmailSenderMock emailSender = new EmailSenderMock();
        MemberRegister register = new MemberModifyService(
                new MemberQueryService(new MemberRepositoryStub()), new MemberRepositoryStub(), emailSender, MemberFixture.createPasswordEncoder()
        );

        Member member = register.register(MemberFixture.createMemberRegisterReques());
        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

        assertThat(emailSender.getTos()).hasSize(1);
        assertThat(emailSender.getTos().getFirst()).isEqualTo(member.getEmail());
    }

    @Test
    @DisplayName("회원 생성 Mockito")
    void registerTestMockito() {
        EmailSender emailSender = Mockito.mock(EmailSender.class);
        MemberRegister register = new MemberModifyService(
                new MemberQueryService(new MemberRepositoryStub()), new MemberRepositoryStub(), emailSender, MemberFixture.createPasswordEncoder()
        );

        Member member = register.register(MemberFixture.createMemberRegisterReques());
        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

        Mockito.verify(emailSender).send(eq(member.getEmail()), any(), any());


    }


    static class MemberRepositoryStub implements MemberRepository {

        @Override
        public Member save(Member member) {
            ReflectionTestUtils.setField(member, "id", 1L);
            return member;
        }

        @Override
        public Optional<Member> findByEmail(Email email) {
            return Optional.empty();
        }

        @Override
        public Optional<Member> findById(Long memberId) {
            return Optional.empty();
        }

    }


    static class EmailSenderStub implements EmailSender {

        @Override
        public void send(Email email, String subject, String body) {
        }
    }

    static class EmailSenderMock implements EmailSender {

        List<Email> tos = new ArrayList<>();

        @Override
        public void send(Email email, String subject, String body) {
            tos.add(email);
        }

        public List<Email> getTos() {
            return tos;
        }
    }
}
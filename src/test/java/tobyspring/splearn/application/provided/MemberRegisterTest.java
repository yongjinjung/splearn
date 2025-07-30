package tobyspring.splearn.application.provided;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import tobyspring.splearn.SplearnTestConfiguration;
import tobyspring.splearn.domain.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@Import({SplearnTestConfiguration.class})
record MemberRegisterTest(MemberRegister memberRegister, EntityManager entityManager) {

    @Test
    @DisplayName("회원 등록")
    void register() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterReques());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

    }

    @Test
    @DisplayName("회원 email 중복 체크")
    void duplicateFail() {
        memberRegister.register(MemberFixture.createMemberRegisterReques());
        assertThatThrownBy(()-> memberRegister.register(MemberFixture.createMemberRegisterReques())).isInstanceOf(DuplicateEmailException.class);
    }

    @Test
    @DisplayName("회원 등록 활성화")
    void activate() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterReques());
        entityManager.flush();
        entityManager.clear();

        Member activate = memberRegister.activate(member.getId());
        entityManager.flush();

        assertThat(activate.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }

    @Test
    @DisplayName("회원 요청 validation")
    void memberRegisterRequestFail() {
        checkValidation(new MemberRegisterRequest("toby@splearn.app","Toby","longsecret "));
        checkValidation(new MemberRegisterRequest("toby@splearn.app","Charlie________________","longsecret "));
        checkValidation(new MemberRegisterRequest("tobysplearn.app","Charlie","longsecret"));

    }

    private void checkValidation(MemberRegisterRequest invalid) {
        assertThatThrownBy(()-> memberRegister.register(invalid)).isInstanceOf(ConstraintViolationException.class);
    }

}

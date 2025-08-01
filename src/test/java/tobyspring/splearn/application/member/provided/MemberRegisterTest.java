package tobyspring.splearn.application.member.provided;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import tobyspring.splearn.SplearnTestConfiguration;
import tobyspring.splearn.domain.member.*;

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

        System.out.println(member.toString());

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
        Member member = registerMember();

        Member activate = memberRegister.activate(member.getId());
        entityManager.flush();

        assertThat(activate.getStatus()).isEqualTo(MemberStatus.ACTIVE);
        assertThat(activate.getDetail().getActivatedAt()).isNotNull();
    }

    @Test
    @DisplayName("회원 탈퇴")
    void deactivate() {
        Member member = registerMember();

        memberRegister.activate(member.getId());
        entityManager.flush();
        entityManager.clear();

        member = memberRegister.deactivate(member.getId());

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
        assertThat(member.getDetail().getDeactivatedAt()).isNotNull();
    }

    @Test
    @DisplayName("회원 정보 업데이트")
    void updateInfo() {
        Member member = registerMember();

        memberRegister.activate(member.getId());
        entityManager.flush();
        entityManager.clear();

        var request = new MemberInfoUpdateRequest("yongjin", "toby100", "자기소개");
        member = memberRegister.updateInfo(member.getId(), request);
        assertThat(member.getDetail().getProfile().address()).isEqualTo("toby100");
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

    private Member registerMember() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterReques());
        entityManager.flush();
        entityManager.clear();
        return member;
    }
}

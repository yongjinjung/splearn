package tobyspring.splearn.application.member.provided;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import tobyspring.splearn.SplearnTestConfiguration;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.member.MemberFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@Import({SplearnTestConfiguration.class})
record MemberFinderTest(MemberRegister memberRegister, MemberFinder memberFinder, EntityManager entityManager) {

    @Test
    @DisplayName("회원 조회")
    void memberFind() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterReques());
        entityManager.flush();
        entityManager.clear();

        Member found = memberFinder.find(member.getId());

        assertThat(member.getId()).isEqualTo(found.getId());
    }

    @Test
    @DisplayName("회원 조회 실패")
    void memberFindFail() {
        assertThatThrownBy(()-> memberFinder.find(999L)).isInstanceOf(IllegalArgumentException.class);
    }
}
package tobyspring.splearn.application.required;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tobyspring.splearn.domain.Member;
import tobyspring.splearn.domain.MemberFixture;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("회원등록")
    void createMember() {
        Member member = Member.register(MemberFixture.createMemberRegisterReques(), MemberFixture.createPasswordEncoder());
        assertThat(member.getId()).isNull();
        memberRepository.save(member);
        assertThat(member.getId()).isNotNull();
        entityManager.flush();
    }
}
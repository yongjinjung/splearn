package tobyspring.splearn.application.required;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import tobyspring.splearn.domain.Member;
import tobyspring.splearn.domain.MemberFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    @DisplayName("회원 이메일 정합성 검사")
    void duplicateEmailFail() {
        Member member = Member.register(MemberFixture.createMemberRegisterReques(), MemberFixture.createPasswordEncoder());
        memberRepository.save(member);

        Member member2 = Member.register(MemberFixture.createMemberRegisterReques(), MemberFixture.createPasswordEncoder());
        assertThatThrownBy(()->memberRepository.save(member2)).isInstanceOf(DataIntegrityViolationException.class);
    }
}
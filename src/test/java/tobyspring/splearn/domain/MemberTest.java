package tobyspring.splearn.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tobyspring.splearn.domain.MemberFixture.*;

class MemberTest {

    Member member;
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        this.passwordEncoder = createPasswordEncoder();
        member = Member.register(createMemberRegisterReques(), passwordEncoder);
    }

    @Test
    @DisplayName("회원 생성")
    void registerMember(){
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    @DisplayName("가입완료")
    void activate() {
        member.activate();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }

    @Test
    @DisplayName("가입실패")
    void activateFail() {
        //give

        //when
        member.activate();

        //then
        assertThatThrownBy(member::activate).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("회원 탈퇴")
    void deactivate() {
        member.activate();
        
        member.deactivate();
        
        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
    }
    
    @Test
    @DisplayName("ACTIVATE 상태가 아닌 상태에서 DEACTIVATED 상태 변경시 에러발생")
    void deactivatedFail() {
        assertThatThrownBy(member::deactivate)
                .isInstanceOf(IllegalStateException.class);

        member.activate();
        member.deactivate();

        assertThatThrownBy(member::deactivate)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("비밀번호를 검증한다.")
    void verifyPassword() {
        assertThat(member.verifyPassword("secret", passwordEncoder)).isTrue();
        assertThat(member.verifyPassword("hello", passwordEncoder)).isFalse();
    }

    @Test
    @DisplayName("닉네임을 변경한다.")
    void changeNickname() {
        assertThat(member.getNickname()).isEqualTo("yongjin");
        member.changeNickname("DragonJin");
        assertThat(member.getNickname()).isEqualTo("DragonJin");

    }
    
    @Test
    @DisplayName("비밀번호를 변경한다.")
    void changePassword() {
        member.changePassword("secret2", passwordEncoder);
        assertThat(member.verifyPassword("secret2", passwordEncoder)).isTrue();
    }

    @Test
    @DisplayName("회원 상태가 활성화 상태인지 체크")
    void isActive() {

        assertThat(member.isActive()).isFalse();

        member.activate();

        assertThat(member.isActive()).isTrue();

        member.deactivate();

        assertThat(member.isActive()).isFalse();

    }

    @Test
    @DisplayName("이메일 검증")
    void invalidEmail() {
        assertThatThrownBy(() -> {
            Member.register(createMemberRegisterRequest("invalid email"), passwordEncoder);
        }).isInstanceOf(IllegalArgumentException.class);

        Member.register(createMemberRegisterRequest("jyj1641@gmail.com"), passwordEncoder);
    }

}
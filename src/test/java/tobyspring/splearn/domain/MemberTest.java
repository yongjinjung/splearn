package tobyspring.splearn.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {

    @Test
    @DisplayName("회원 생성")
    void createMember(){
        var member = new Member("jyj1641@gmail.com", "드래곤청바지", "secret");

        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }


    @Test
    @DisplayName("필수 항목 체크")
    void requiredCheck() {
        assertThatThrownBy(() -> new Member(null, "DragonJin", "secret"))
                .isInstanceOf(
                        NullPointerException.class
                );
    }

    @Test
    @DisplayName("가입완료")
    void activate() {
        var member = new Member("toby", "Toby", "secret");
        member.activate();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }

    @Test
    @DisplayName("가입실패")
    void activateFail() {
        //give
        var member = new Member("toby", "Toby", "secret");

        //when
        member.activate();

        //then
        assertThatThrownBy(()-> {
            member.activate();
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("회원 탈퇴")
    void deactivate() {
        var member = new Member("toby", "Toby", "secret");
        member.activate();
        
        member.deactivate();
        
        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
    }
    
    @Test
    @DisplayName("ACTIVATE 상태가 아닌 상태에서 DEACTIVATED 상태 변경시 에러발생")
    void deactivatedFail() {
        var member = new Member("toby", "Toby", "secret");
        assertThatThrownBy(member::deactivate)
                .isInstanceOf(IllegalStateException.class);

        member.activate();
        member.deactivate();

        assertThatThrownBy(member::deactivate)
                .isInstanceOf(IllegalStateException.class);
    }
}
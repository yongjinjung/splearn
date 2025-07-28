package tobyspring.splearn.application.provided;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import tobyspring.splearn.SplearnTestConfiguration;
import tobyspring.splearn.domain.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Import({SplearnTestConfiguration.class})
public class MemberRegisterTest {

    @Autowired
    private MemberRegister memberRegister;

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
        Member member = memberRegister.register(MemberFixture.createMemberRegisterReques());
        assertThatThrownBy(()-> memberRegister.register(MemberFixture.createMemberRegisterReques())).isInstanceOf(DuplicateEmailException.class);
    }

}

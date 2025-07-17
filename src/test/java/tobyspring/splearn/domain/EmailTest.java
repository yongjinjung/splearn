package tobyspring.splearn.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


class EmailTest {

    @Test
    @DisplayName("이메일 동등한지 검증")
    void equality() {
        Email email = new Email("tobyspring@gmail.com");
        Email email2 = new Email("tobyspring@gmail.com");
        assertThat(email).isEqualTo(email2);
    }

}
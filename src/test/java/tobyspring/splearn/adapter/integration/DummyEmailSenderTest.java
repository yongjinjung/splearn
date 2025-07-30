package tobyspring.splearn.adapter.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;
import tobyspring.splearn.domain.Email;

class DummyEmailSenderTest {
    @Test
    @DisplayName("더미 이메일 발송")
    @StdIo
    void dummyEmailSender(StdOut out) {
        DummyEmailSender dummyEmailSender = new DummyEmailSender();
        dummyEmailSender.send(new Email("toby@splearn.app"), "subject", "body");

        Assertions.assertThat(out.capturedLines()[0])
                .isEqualTo("DummyEmailSender send email: Email[address=toby@splearn.app]");
    }

}
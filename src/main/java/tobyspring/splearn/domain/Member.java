package tobyspring.splearn.domain;

import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

import static org.springframework.util.Assert.state;

@Getter
@ToString
public class Member {

    String email;

    String nickname;

    String passwordHash;

    MemberStatus status;

    public Member(String email, String nickname, String passwordHash) {
        this.email = Objects.requireNonNull(email);
        this.nickname = Objects.requireNonNull(nickname);
        this.passwordHash = Objects.requireNonNull(passwordHash);

        this.status = MemberStatus.PENDING;
    }

    public void activate() {
        state(status == MemberStatus.PENDING, "PENDING 상태가 아닙니다.");

        this.status = MemberStatus.ACTIVE;
    }

    public void deactivate() {
        state(status == MemberStatus.ACTIVE, "ACTIVE 상태가 아닙니다.");

        this.status = MemberStatus.DEACTIVATED;
    }
}

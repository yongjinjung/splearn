package tobyspring.splearn.domain.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import tobyspring.splearn.domain.AbstractEntity;
import tobyspring.splearn.domain.shared.Email;

import java.util.Objects;

import static org.springframework.util.Assert.state;

@Entity
@Getter
@ToString(callSuper = true, exclude = "detail")
@NaturalIdCache
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends AbstractEntity {

    /** 닉네임 **/
    @NaturalId
    private Email email;

    /** 닉네임 **/
    private String nickname;

    /** 비밀번호 **/
    private String passwordHash;

    /** 회원 상태 **/
    private MemberStatus status;

    /** 회원 상세 **/
    private MemberDetail detail;

    public static Member register(MemberRegisterRequest createRequest, PasswordEncoder passwordEncoder){
        Member member = new Member();

        member.email = new Email(createRequest.email());
        member.nickname = Objects.requireNonNull(createRequest.nickname());
        member.passwordHash = Objects.requireNonNull(passwordEncoder.encode(createRequest.password()));

        member.status = MemberStatus.PENDING;

        member.detail = MemberDetail.create();

        return member;
    }

    public void activate() {
        state(status == MemberStatus.PENDING, "PENDING 상태가 아닙니다.");

        this.status = MemberStatus.ACTIVE;

        detail.activate();
    }

    public void deactivate() {
        state(status == MemberStatus.ACTIVE, "ACTIVE 상태가 아닙니다.");

        this.status = MemberStatus.DEACTIVATED;

        detail.deactivate();
    }

    public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {

        return passwordEncoder.matches(password, this.passwordHash);
    }

    public void changeNickname(String nickname) {
        this.nickname = Objects.requireNonNull(nickname);
    }

    public void updateInfo(MemberInfoUpdateRequest updateRequest){
        state(status == MemberStatus.ACTIVE, "활성화된 회원만 정보 수정이 가능합니다.");

        this.nickname = Objects.requireNonNull(updateRequest.nickname());

        this.detail.updateInfo(updateRequest);
    }

    public void changePassword(String password, PasswordEncoder passwordEncoder) {
        this.passwordHash = passwordEncoder.encode(Objects.requireNonNull(password));
    }

    public boolean isActive() {
        return this.status == MemberStatus.ACTIVE;
    }
}

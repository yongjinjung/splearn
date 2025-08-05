package tobyspring.splearn.application.member.required;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.member.Profile;
import tobyspring.splearn.domain.shared.Email;

import java.util.Optional;

/**
 * 회원정보를 저장한다.
 */
public interface MemberRepository extends Repository<Member, Long> {
    Member save(Member member);

    Optional<Member> findByEmail(Email email);

    Optional<Member> findById(Long memberId);

    @Query("select m from Member m where m.detail.profile = :profile")
    Optional<Member> findByProfile(Profile profile);
}

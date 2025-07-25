package tobyspring.splearn.application.required;

import org.springframework.data.repository.Repository;
import tobyspring.splearn.domain.Member;

/**
 * 회원정보를 저장한다.
 */
public interface MemberRepository extends Repository<Member, Long> {
    Member save(Member member);
}

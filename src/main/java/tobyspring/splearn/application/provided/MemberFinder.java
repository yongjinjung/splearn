package tobyspring.splearn.application.provided;

import tobyspring.splearn.domain.Member;

public interface MemberFinder {
    Member find(Long memberId);
}

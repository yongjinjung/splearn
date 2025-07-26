package tobyspring.splearn.domain;

public class MemberFixture {

    public static  MemberRegisterRequest createMemberRegisterRequest(String invalidEmail) {
        return new MemberRegisterRequest(invalidEmail, "yongjin", "secret");
    }

    public static MemberRegisterRequest createMemberRegisterReques() {
        return createMemberRegisterRequest("toby@splearn.app");
    }

     public static PasswordEncoder createPasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(String password) {
                return password.toUpperCase();
            }

            @Override
            public boolean matches(String password, String passwordHash) {
                return encode(password).equals(passwordHash);
            }
        };
    }

}

package tobyspring.splearn.domain.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProfileTest {
    @Test
    @DisplayName("프로필")
    void profile() {
        new Profile("tovbt");
        new Profile("toby100");
        new Profile("12345");
        new Profile("");
    }

    @Test
    @DisplayName("프로필 실패")
    void profileFail() {
        assertThatThrownBy(()-> new Profile("toolongtoolongtoolongtoolongtoolongtoolongtoolong")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(()-> new Profile("A")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(()-> new Profile("프로필")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("프로필 URL")
    void profileUrl() {
        var profile = new Profile("yongjin009");
        assertThat(profile.url()).isEqualTo("@yongjin009");
    }

}
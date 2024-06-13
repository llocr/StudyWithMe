package december.spring.studywithme.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {
    @Test
    public void testUser() {
        // Given
        User user = User.builder()
                .userId("kyj1234567")
                .password("Testpassword123!")
                .name("backend")
                .email("backend@naver.com")
                .introduce("personal project")
                .build();

        // When
        String userId = user.getUserId();
        String password = user.getPassword();
        String name = user.getName();
        String email = user.getEmail();
        String introduce = user.getIntroduce();

        // Then
        assertThat(userId).isEqualTo("kyj1234567");
        assertThat(password).isEqualTo("Testpassword123!");
        assertThat(name).isEqualTo("backend");
        assertThat(email).isEqualTo("backend@naver.com");
        assertThat(introduce).isEqualTo("personal project");
    }
}
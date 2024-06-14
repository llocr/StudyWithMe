package december.spring.studywithme.unit.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import december.spring.studywithme.entity.User;
import december.spring.studywithme.entity.UserType;
import december.spring.studywithme.utils.MonkeyUtils;

class UserTest {
	
	User createUser() {
		return MonkeyUtils.commonMonkey().giveMeOne(User.class);
	}
	
	@Test
	@DisplayName("유저 생성 테스트")
	void 유저생성() {
	    //given
		String userId = "testUser111";
		String password = "Hihello1234!";
		String name = "희수";
		String email = "36-96@email.com";
		UserType userType = UserType.UNVERIFIED;
		LocalDateTime statusChangedAt = LocalDateTime.now();
		
	    //when
		User user = User.builder()
			.userId(userId)
			.password(password)
			.name(name)
			.email(email)
			.userType(userType)
			.statusChangedAt(statusChangedAt)
			.build();
	    
	    //then
		assertThat(user.getUserId()).isEqualTo(userId);
		assertThat(user.getPassword()).isEqualTo(password);
		assertThat(user.getName()).isEqualTo(name);
		assertThat(user.getEmail()).isEqualTo(email);
		assertThat(user.getUserType()).isEqualTo(userType);
		assertThat(user.getStatusChangedAt()).isEqualTo(statusChangedAt);
	}
	
	@Test
	@DisplayName("유저 상태 변경 - 탈퇴")
	void 유저상태변경_탈퇴 () {
	    //given
		User user = createUser();
		
		//when
		user.withdrawUser();
	    
	    //then
		assertThat(user.getUserType()).isEqualTo(UserType.DEACTIVATED);
	}
	
	@Test
	@DisplayName("유저 상태 변경 - 인증")
	void 유저상태변경_인증() {
	    //given
	    User user = createUser();
		
	    //when
		user.ActiveUser();
	    
	    //then
		assertThat(user.getUserType()).isEqualTo(UserType.ACTIVE);
	}
	
	@Test
	@DisplayName("리프레시 토큰 초기화")
	void 리프레시토큰초기화() {
	    //given
		User user = createUser();
		String refreshToken = "refreshToken";
		
	    //when
		user.refreshTokenReset(refreshToken);
	    
	    //then
		assertThat(user.getRefreshToken()).isEqualTo(refreshToken);
	}
	
	@Test
	@DisplayName("프로필 수정")
	void 프로필수정() {
	    //given
	    User user = createUser();
		String name = "수희";
		String introduce = "hello world!";
		
	    //when
		user.editProfile(name, introduce);
	    
	    //then
		assertThat(user.getName()).isEqualTo(name);
		assertThat(user.getIntroduce()).isEqualTo(introduce);
	}
	
	@Test
	@DisplayName("비밀번호 수정")
	void 비밀번호수정() {
	    //given
	    User user = createUser();
		String newPassword = "newPassword1212!";
		
	    //when
		user.changePassword(newPassword);
	    
	    //then
		assertThat(user.getPassword()).isEqualTo(newPassword);
	}
	
}
package december.spring.studywithme.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserProfileRequestDTOTest {
	@Test
	@DisplayName("UserProfileRequestDTO 생성 테스트")
	void DTO생성() {
	    //given
		UserProfileRequestDTO userProfileRequestDTO = new UserProfileRequestDTO();
	    String userId = "testId";
		String name = "testName";
		String introduce = "introduce";
		String currentPassword = "1234567890";
		
	    //when
		userProfileRequestDTO.setUserId(userId);
		userProfileRequestDTO.setName(name);
		userProfileRequestDTO.setIntroduce(introduce);
		userProfileRequestDTO.setCurrentPassword(currentPassword);
	    
	    //then
		assertThat(userProfileRequestDTO.getUserId()).isEqualTo(userId);
		assertThat(userProfileRequestDTO.getName()).isEqualTo(name);
		assertThat(userProfileRequestDTO.getIntroduce()).isEqualTo(introduce);
		assertThat(userProfileRequestDTO.getCurrentPassword()).isEqualTo(currentPassword);
	}
}
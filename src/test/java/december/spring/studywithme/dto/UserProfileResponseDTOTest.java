package december.spring.studywithme.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import december.spring.studywithme.entity.User;
import december.spring.studywithme.utils.MonkeyUtils;

class UserProfileResponseDTOTest {
	@Test
	@DisplayName("UserProfileResponseDTO 생성 테스트")
	void DTO생성() {
	    //given
	    User user = MonkeyUtils.commonMonkey().giveMeOne(User.class);
		
	    //when
		UserProfileResponseDTO userProfileResponseDTO = new UserProfileResponseDTO(user);
	    
	    //then
		assertThat(userProfileResponseDTO.getUserId()).isEqualTo(user.getUserId());
		assertThat(userProfileResponseDTO.getEmail()).isEqualTo(user.getEmail());
		assertThat(userProfileResponseDTO.getIntroduce()).isEqualTo(user.getIntroduce());
	}
}
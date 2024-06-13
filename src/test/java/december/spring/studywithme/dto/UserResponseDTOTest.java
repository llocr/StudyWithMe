package december.spring.studywithme.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import december.spring.studywithme.entity.User;
import december.spring.studywithme.utils.MonkeyUtils;

class UserResponseDTOTest {
	@Test
	@DisplayName("UserResponseDTO 생성 테스트")
	void DTO생성() {
	    //given
	    User user = MonkeyUtils.commonMonkey().giveMeOne(User.class);
		
	    //when
		UserResponseDTO userResponseDTO = new UserResponseDTO(user);
	    
	    //then
		assertThat(userResponseDTO.getUserId()).isEqualTo(user.getUserId());
		assertThat(userResponseDTO.getName()).isEqualTo(user.getName());
		assertThat(userResponseDTO.getEmail()).isEqualTo(user.getEmail());
		assertThat(userResponseDTO.getIntroduce()).isEqualTo(user.getIntroduce());
	}
}
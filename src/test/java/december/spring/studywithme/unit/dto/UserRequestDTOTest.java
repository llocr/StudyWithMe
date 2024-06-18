package december.spring.studywithme.unit.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import december.spring.studywithme.dto.UserRequestDTO;

class UserRequestDTOTest {
	@Test
	@DisplayName("UserRequestDTO 생성 테스트")
	void DTO생성() {
	    //given
		UserRequestDTO userRequestDTO = new UserRequestDTO();
	    String userId = "testId";
		String password = "1234567890";
		String name = "testName";
		String email = "12345@gmail.com";
		String introduce = "introduce";
		
	    //when
	    userRequestDTO.setUserId(userId);
		userRequestDTO.setPassword(password);
		userRequestDTO.setName(name);
		userRequestDTO.setEmail(email);
		userRequestDTO.setIntroduce(introduce);
		
	    //then
		Assertions.assertThat(userRequestDTO.getUserId()).isEqualTo(userId);
		Assertions.assertThat(userRequestDTO.getPassword()).isEqualTo(password);
		Assertions.assertThat(userRequestDTO.getName()).isEqualTo(name);
		Assertions.assertThat(userRequestDTO.getEmail()).isEqualTo(email);
		Assertions.assertThat(userRequestDTO.getIntroduce()).isEqualTo(introduce);
	}
}
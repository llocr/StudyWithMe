package december.spring.studywithme.unit.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import december.spring.studywithme.dto.LoginRequestDTO;

class LoginRequestDTOTest {
	@Test
	@DisplayName("LoginRequestDTO 생성 테스트")
	void DTO생성() {
	    //given
		LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
	    String userId = "testId";
		String password = "testPassword";
		
	    //when
		loginRequestDTO.setUserId(userId);
		loginRequestDTO.setPassword(password);
	    
	    //then
		assertThat(loginRequestDTO.getUserId()).isEqualTo(userId);
	}
}
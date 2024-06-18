package december.spring.studywithme.unit.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import december.spring.studywithme.dto.PasswordRequestDTO;

class PasswordRequestDTOTest {
	@Test
	@DisplayName("PasswordRequestDTO 생성 테스트")
	void DTO생성() {
	    //given
	    PasswordRequestDTO passwordRequestDTO = new PasswordRequestDTO();
		String password = "1234567890";
		
	    //when
		passwordRequestDTO.setPassword(password);
		
	    //then
		Assertions.assertThat(passwordRequestDTO.getPassword()).isEqualTo(password);
	}
	
}
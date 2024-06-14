package december.spring.studywithme.unit.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import december.spring.studywithme.dto.ResponseMessage;
import december.spring.studywithme.dto.UserResponseDTO;
import december.spring.studywithme.utils.MonkeyUtils;

class ResponseMessageTest {
	@Test
	@DisplayName("ResponseMessage 생성 테스트")
	void ResponseMessage생성() {
	    //given
	    Integer status = HttpStatus.OK.value();
		String message = "test";
		UserResponseDTO data = MonkeyUtils.commonMonkey().giveMeOne(UserResponseDTO.class);
		
	    //when
		ResponseMessage<UserResponseDTO> responseMessage = ResponseMessage.<UserResponseDTO>builder()
			.statusCode(status)
			.message(message)
			.data(data)
			.build();
	    
	    //then
		Assertions.assertThat(responseMessage.getStatusCode()).isEqualTo(status);
		Assertions.assertThat(responseMessage.getMessage()).isEqualTo(message);
		Assertions.assertThat(responseMessage.getData()).isEqualTo(data);
	}
}
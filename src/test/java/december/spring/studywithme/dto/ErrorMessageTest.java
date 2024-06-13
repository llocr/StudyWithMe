package december.spring.studywithme.dto;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class ErrorMessageTest {
	@Test
	@DisplayName("ErrorMessage 생성 테스트")
	void ErrorMessage생성() {
	    //given
	    Integer statusCode = HttpStatus.OK.value();
		String message = "테스트 메시지";
		
	    //when
	    ErrorMessage errorMessage = ErrorMessage.builder()
			.statusCode(statusCode)
			.message(message)
			.build();
		
	    //then
		assertThat(errorMessage.getStatusCode()).isEqualTo(statusCode);
		assertThat(errorMessage.getMessage()).isEqualTo(message);
	}
	
}
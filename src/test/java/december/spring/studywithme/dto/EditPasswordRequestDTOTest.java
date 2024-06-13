package december.spring.studywithme.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EditPasswordRequestDTOTest {
	@Test
	@DisplayName("EditPasswordRequestDTO 생성 테스트")
	void DTO생성() {
	    //given
		EditPasswordRequestDTO editPasswordRequestDTO = new EditPasswordRequestDTO();
	    String currentPassword = "1234567890";
		String newPassword = "abcd123456!";
		
	    //when
	    editPasswordRequestDTO.setCurrentPassword(currentPassword);
		editPasswordRequestDTO.setNewPassword(newPassword);
		
	    //then
		Assertions.assertThat(editPasswordRequestDTO.getCurrentPassword()).isEqualTo(currentPassword);
		Assertions.assertThat(editPasswordRequestDTO.getNewPassword()).isEqualTo(newPassword);
	}
}
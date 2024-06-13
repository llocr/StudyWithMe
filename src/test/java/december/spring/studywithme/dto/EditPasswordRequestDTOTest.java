package december.spring.studywithme.dto;

import static org.assertj.core.api.Assertions.assertThat;

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
		assertThat(editPasswordRequestDTO.getCurrentPassword()).isEqualTo(currentPassword);
		assertThat(editPasswordRequestDTO.getNewPassword()).isEqualTo(newPassword);
	}
}
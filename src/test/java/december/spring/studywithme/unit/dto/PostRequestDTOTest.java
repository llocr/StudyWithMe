package december.spring.studywithme.unit.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import december.spring.studywithme.dto.PostRequestDTO;

class PostRequestDTOTest {
	@Test
	@DisplayName("PostRequestDTO 생성 테스트")
	void DTO생성() {
	    //given
	    PostRequestDTO postRequestDTO = new PostRequestDTO();
		String title = "title";
		String contents = "contents";
		
	    //when
		postRequestDTO.setTitle(title);
		postRequestDTO.setContents(contents);
	    
	    //then
		assertThat(postRequestDTO.getTitle()).isEqualTo(title);
		assertThat(postRequestDTO.getContents()).isEqualTo(contents);
	}
	
}
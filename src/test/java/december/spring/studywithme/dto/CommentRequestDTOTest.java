package december.spring.studywithme.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CommentRequestDTOTest {
	@Test
	@DisplayName("CommentRequestDTO 생성 테스트")
	void DTO생성() {
	    //given
		CommentRequestDTO commentRequestDTO = new CommentRequestDTO();
	    String contents = "댓글 내용";
		
	    //when
		commentRequestDTO.setContents(contents);
	    
	    //then
		assertThat(commentRequestDTO.getContents()).isEqualTo(contents);
	}
}
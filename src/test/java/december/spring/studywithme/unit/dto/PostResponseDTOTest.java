package december.spring.studywithme.unit.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import december.spring.studywithme.dto.PostResponseDTO;
import december.spring.studywithme.entity.Post;
import december.spring.studywithme.entity.User;
import december.spring.studywithme.utils.MonkeyUtils;

class PostResponseDTOTest {
	@Test
	@DisplayName("PostResponseDTO 생성 테스트")
	void DTO생성() {
	    //given
		User user = MonkeyUtils.commonMonkey().giveMeOne(User.class);
		Post post = Post.builder().user(user).title("title").contents("content").build();
		
	    //when
	    PostResponseDTO postResponseDTO = new PostResponseDTO(post);
		
	    //then
		assertThat(postResponseDTO.getUserId()).isEqualTo(post.getUser().getUserId());
		assertThat(postResponseDTO.getTitle()).isEqualTo(post.getTitle());
		assertThat(postResponseDTO.getContents()).isEqualTo(post.getContents());
	}
}
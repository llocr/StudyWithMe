package december.spring.studywithme.unit.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import december.spring.studywithme.dto.CommentResponseDTO;
import december.spring.studywithme.entity.Comment;
import december.spring.studywithme.entity.Post;
import december.spring.studywithme.entity.User;
import december.spring.studywithme.utils.MonkeyUtils;

class CommentResponseDTOTest {
	@Test
	@DisplayName("CommentResponseDTO 생성 테스트")
	void DTO생성() {
	    //given
		User user = MonkeyUtils.commonMonkey().giveMeOne(User.class);
		Post post = MonkeyUtils.commonMonkey().giveMeOne(Post.class);
		
	    Comment comment = Comment.builder()
			.user(user)
			.post(post)
			.contents("댓글 내용")
			.build();
		
	    //when
		CommentResponseDTO commentResponseDTO = new CommentResponseDTO(comment);
	    
	    //then
		assertThat(commentResponseDTO.getUserId()).isEqualTo(comment.getUser().getUserId());
		assertThat(commentResponseDTO.getPostId()).isEqualTo(comment.getPost().getId());
		assertThat(commentResponseDTO.getContents()).isEqualTo(comment.getContents());
	}
}
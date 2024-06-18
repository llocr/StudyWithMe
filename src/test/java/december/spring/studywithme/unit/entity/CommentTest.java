package december.spring.studywithme.unit.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import december.spring.studywithme.dto.CommentRequestDTO;
import december.spring.studywithme.entity.Comment;
import december.spring.studywithme.entity.Post;
import december.spring.studywithme.entity.User;
import december.spring.studywithme.utils.MonkeyUtils;

class CommentTest {
	Comment createComment() {
		return MonkeyUtils.commonMonkey().giveMeOne(Comment.class);
	}
	
	@Test
	@DisplayName("댓글 생성 테스트")
	void 댓글생성() {
	    //given
		Post post = MonkeyUtils.commonMonkey().giveMeOne(Post.class);
		User user = MonkeyUtils.commonMonkey().giveMeOne(User.class);
		String contents = "댓글 내용입니다.";
		
	    //when
		Comment comment = Comment.builder()
			.post(post)
			.user(user)
			.contents(contents)
			.build();
	    
	    //then
		assertThat(comment.getPost()).isEqualTo(post);
		assertThat(comment.getUser()).isEqualTo(user);
		assertThat(comment.getContents()).isEqualTo(contents);
	}
	
	@Test
	@DisplayName("댓글 수정 테스트")
	void 댓글수정() {
	    //given
	    Comment comment = createComment();
		
	    //when
		CommentRequestDTO requestDTO = MonkeyUtils.commonMonkey().giveMeOne(CommentRequestDTO.class);
		comment.update(requestDTO);
	    
	    //then
		assertThat(comment.getContents()).isEqualTo(requestDTO.getContents());
	}
	
	@Test
	@DisplayName("댓글 좋아요 수정 테스트")
	void 댓글좋아요수정() {
	    //given
	    Comment comment = createComment();
		
	    //when
		Long likes = 10L;
		comment.updateCommentLikes(likes);
	    
	    //then
		assertThat(comment.getLikes()).isEqualTo(likes);
	}
}
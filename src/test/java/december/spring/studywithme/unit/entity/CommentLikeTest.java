package december.spring.studywithme.unit.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import december.spring.studywithme.entity.Comment;
import december.spring.studywithme.entity.CommentLike;
import december.spring.studywithme.entity.User;
import december.spring.studywithme.utils.MonkeyUtils;

class CommentLikeTest {
	CommentLike createCommentLike() {
		return MonkeyUtils.commonMonkey().giveMeOne(CommentLike.class);
	}
	
	@Test
	@DisplayName("댓글 좋아요 생성 테스트")
	void 댓글좋아요생성() {
	    //given
	    User user = MonkeyUtils.commonMonkey().giveMeOne(User.class);
		Comment comment = MonkeyUtils.commonMonkey().giveMeOne(Comment.class);
		boolean isLike = true;
		
	    //when
	    CommentLike commentLike = CommentLike.builder()
			.user(user)
			.comment(comment)
			.isLike(isLike)
			.build();
		
	    //then
		assertThat(commentLike.getUser()).isEqualTo(user);
		assertThat(commentLike.getComment()).isEqualTo(comment);
		assertThat(commentLike.isLike()).isEqualTo(isLike);
	}
	
	@Test
	@DisplayName("댓글 좋아요 수정 테스트")
	void 댓글좋아요수정() {
	    //given
	    CommentLike commentLike = createCommentLike();
		boolean isLike = commentLike.isLike();
		
	    //when
	    commentLike.update();
		
	    //then
		assertThat(commentLike.isLike()).isNotEqualTo(isLike);
	}
}
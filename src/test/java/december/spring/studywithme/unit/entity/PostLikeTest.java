package december.spring.studywithme.unit.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import december.spring.studywithme.entity.Post;
import december.spring.studywithme.entity.PostLike;
import december.spring.studywithme.entity.User;
import december.spring.studywithme.utils.MonkeyUtils;

class PostLikeTest {
	@Test
	@DisplayName("게시글 좋아요 생성 테스트")
	void 게시글좋아요생성() {
	    //given
	    User user = MonkeyUtils.commonMonkey().giveMeOne(User.class);
		Post post = MonkeyUtils.commonMonkey().giveMeOne(Post.class);
		boolean isLike = true;
		
	    //when
		PostLike postLike = PostLike.builder()
			.user(user)
			.post(post)
			.isLike(isLike)
			.build();
	    
	    //then
		assertThat(postLike.getUser()).isEqualTo(user);
		assertThat(postLike.getPost()).isEqualTo(post);
		assertThat(postLike.isLike()).isEqualTo(isLike);
	}
}
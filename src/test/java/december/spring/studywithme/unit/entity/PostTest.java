package december.spring.studywithme.unit.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import december.spring.studywithme.dto.PostRequestDTO;
import december.spring.studywithme.entity.Post;
import december.spring.studywithme.entity.User;
import december.spring.studywithme.utils.MonkeyUtils;

class PostTest {
	Post createPost() {
		return MonkeyUtils.commonMonkey().giveMeOne(Post.class);
	}
	
	@Test
	@DisplayName("게시글 생성 테스트")
	void 게시글생성() {
	    //given
	    User user = MonkeyUtils.commonMonkey().giveMeOne(User.class);
		String title = "게시글 제목입니다.";
		String contents = "게시글 내용입니다.";
		
	    //when
		Post post = Post.builder()
			.user(user)
			.title(title)
			.contents(contents)
			.build();
	    
	    //then
		assertThat(post.getUser()).isEqualTo(user);
		assertThat(post.getTitle()).isEqualTo(title);
		assertThat(post.getContents()).isEqualTo(contents);
	}
	
	@Test
	@DisplayName("게시글 수정 테스트")
	void 게시글수정() {
	    //given
	    Post post = createPost();
		
	    //when
		PostRequestDTO requestDTO = MonkeyUtils.commonMonkey().giveMeOne(PostRequestDTO.class);
		post.update(requestDTO);
	    
	    //then
		assertThat(post.getTitle()).isEqualTo(requestDTO.getTitle());
		assertThat(post.getContents()).isEqualTo(requestDTO.getContents());
	}
	
	@Test
	@DisplayName("게시글 좋아요 수정 테스트")
	void 게시글좋아요수정() {
	    //given
		Post post = createPost();
		Long likes = 10L;
		
	    //when
	    post.updatePostLikes(likes);
		
	    //then
		assertThat(post.getLikes()).isEqualTo(likes);
	}
}
package december.spring.studywithme.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import december.spring.studywithme.entity.Comment;
import december.spring.studywithme.entity.Post;
import december.spring.studywithme.entity.User;
import december.spring.studywithme.entity.UserType;
import december.spring.studywithme.exception.LikeException;
import december.spring.studywithme.repository.CommentLikeRepository;
import december.spring.studywithme.repository.PostLikeRepository;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {
	@Mock
	PostService postService;
	
	@Mock
	CommentService commentService;
	
	@Mock
	PostLikeRepository postLikeRepository;
	
	@Mock
	CommentLikeRepository commentLikeRepository;
	
	@InjectMocks
	LikeService likeService;
	
	public User createUser() {
		return User.builder()
			.userId("testId")
			.password("encodedPassword")
			.name("testName")
			.email("dfdf@gmail.com")
			.userType(UserType.UNVERIFIED)
			.introduce("testIntroduce")
			.build();
	}
	
	public Post createPost() {
		return Post.builder()
			.title("testTitle")
			.contents("testContents")
			.user(new User().builder().userId("testest").build())
			.build();
	}
	
	public Comment createComment() {
		return Comment.builder()
			.contents("testContents")
			.user(new User().builder().userId("testest").build())
			.post(createPost())
			.build();
	}
	
	@Test
	@DisplayName("게시글 좋아요 등록 성공")
	void 게시글좋아요등록성공() {
	    //given
		Long id = 1L;
	    User user = createUser();
		Post post = createPost();
		
		when(postService.getValidatePost(id)).thenReturn(post);
		
	    //when
		boolean result = likeService.likePost(id, user);
	    
	    //then
		assertThat(result).isTrue();
	}
	
	@Test
	@DisplayName("게시글 좋아요 등록 실패 - 본인 게시글에 좋아요 등록 시도")
	void 게시글좋아요등록실패() {
	    //given
		Long id = 1L;
		User user = createUser();
		Post post = Post.builder()
			.user(user)
			.build();
		
		when(postService.getValidatePost(id)).thenReturn(post);
		
	    //when - then
		assertThatThrownBy(() -> likeService.likePost(id, user))
			.isInstanceOf(LikeException.class)
			.hasMessage("본인이 작성한 게시글에는 좋아요를 남길 수 없습니다.");
	}
	
	@Test
	@DisplayName("댓글 좋아요 등록 성공")
	void 댓글좋아요등록성공() {
	    //given
		Long commentId = 1L;
		User user = createUser();
		Post post = createPost();
		Comment comment = createComment();
		
		when(postService.getValidatePost(post.getId())).thenReturn(post);
		when(commentService.getValidateComment(post.getId(), commentId)).thenReturn(comment);
		
		// When
		boolean result = likeService.likeComment(post.getId(), commentId, user);
		
		// Then
		
		//then
		assertThat(result).isTrue();
	}
	
	@Test
	@DisplayName("댓글 좋아요 등록 실패 - 본인 댓글에 좋아요 등록 시도")
	void 댓글좋아요등록실패() {
	    //given
		Long commentId = 1L;
		User user = createUser();
		Post post = createPost();
		Comment comment = Comment.builder()
			.contents("testContents")
			.user(user)
			.post(post)
			.build();
		
		when(postService.getValidatePost(post.getId())).thenReturn(post);
		when(commentService.getValidateComment(post.getId(), commentId)).thenReturn(comment);
		
	    //when - then
		assertThatThrownBy(() -> likeService.likeComment(post.getId(), commentId, user))
			.isInstanceOf(LikeException.class)
			.hasMessage("본인이 작성한 댓글에는 좋아요를 남길 수 없습니다.");
	}
	
}
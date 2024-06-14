package december.spring.studywithme.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import december.spring.studywithme.dto.CommentRequestDTO;
import december.spring.studywithme.dto.CommentResponseDTO;
import december.spring.studywithme.entity.Comment;
import december.spring.studywithme.entity.Post;
import december.spring.studywithme.entity.User;
import december.spring.studywithme.entity.UserType;
import december.spring.studywithme.exception.CommentException;
import december.spring.studywithme.exception.NoContentException;
import december.spring.studywithme.exception.PostException;
import december.spring.studywithme.repository.CommentRepository;
import december.spring.studywithme.service.CommentService;
import december.spring.studywithme.service.PostService;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
	@Mock
	CommentRepository commentRepository;
	
	@Mock
	PostService postService;
	
	@InjectMocks
	CommentService commentService;
	
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
			.user(createUser())
			.build();
	}
	
	public Comment createComment() {
		return Comment.builder()
			.contents("testContents")
			.user(createUser())
			.post(createPost())
			.build();
	}
	
	private void setCommentList(Post post, List<Comment> comments) throws NoSuchFieldException, IllegalAccessException {
		Field commentListField = Post.class.getDeclaredField("commentList");
		commentListField.setAccessible(true);
		commentListField.set(post, comments);
	}
	
	
	@Test
	@DisplayName("댓글 등록 성공")
	void 댓글등록성공() {
	    //given
		User user = createUser();
		Long id = 1L;
		CommentRequestDTO requestDto = new CommentRequestDTO();
		requestDto.setContents("testContents");
		Post post = createPost();
		
		when(postService.getValidatePost(id)).thenReturn(post);
		
	    //when
		CommentResponseDTO commentResponseDTO = commentService.createComment(user, id, requestDto);
		
		//then
		assertThat(commentResponseDTO.getContents()).isEqualTo(requestDto.getContents());
		assertThat(commentResponseDTO.getUserId()).isEqualTo(user.getUserId());
	}
	
	@Test
	@DisplayName("댓글 등록 실패 - 게시글 없음")
	void 댓글등록실패_게시글없음() {
	    //given
	    User user = createUser();
		Long id = 1L;
		CommentRequestDTO requestDto = new CommentRequestDTO();
		requestDto.setContents("testContents");
		
		when(postService.getValidatePost(id)).thenThrow(new PostException("게시글이 존재하지 않습니다."));
		
	    //when - then
		assertThat(assertThrows(PostException.class, () -> commentService.createComment(user, id, requestDto)))
			.isInstanceOf(PostException.class)
			.hasMessageContaining("게시글이 존재하지 않습니다.");
	}
	
	@Test
	@DisplayName("전체 댓글 조회 성공")
	void 전체댓글조회성공() throws NoSuchFieldException, IllegalAccessException {
	    //given
	    Long id = 1L;
		Post post = createPost();
		Comment comment1 = createComment();
		Comment comment2 = createComment();
		setCommentList(post, Arrays.asList(comment1, comment2));
		
		when(postService.getValidatePost(id)).thenReturn(post);
		
	    //when
		List<CommentResponseDTO> commentResponseDTOList = commentService.getAllComments(id);
		
	    //then
		assertThat(commentResponseDTOList).isNotNull();
		assertThat(commentResponseDTOList.size()).isEqualTo(2);
		assertThat(commentResponseDTOList.get(0).getContents()).isEqualTo(comment1.getContents());
	}
	
	@Test
	@DisplayName("전체 댓글 조회 실패 - 댓글 없음")
	void 전체댓글조회실패_댓글없음() throws NoSuchFieldException, IllegalAccessException {
	    //given
		Long id = 1L;
		Post post = createPost();
		setCommentList(post, Collections.emptyList());
		
		when(postService.getValidatePost(id)).thenReturn(post);
		
	    //when - then
		assertThatThrownBy(() -> commentService.getAllComments(id))
			.isInstanceOf(NoContentException.class)
			.hasMessageContaining("가장 먼저 댓글을 작성해보세요!");
	}
	
	@Test
	@DisplayName("단일 댓글 조회 성공")
	void 단일댓글조회성공() {
	    //given
		Long commentId = 1L;
		Post post = createPost();
		Comment comment = createComment();
		
		when(postService.getValidatePost(post.getId())).thenReturn(post);
		when(commentRepository.findByPostIdAndId(post.getId(), commentId)).thenReturn(Optional.of(comment));
		
	    //when
		CommentResponseDTO commentResponseDTO = commentService.getComment(post.getId(), commentId);
	    
	    //then
		assertThat(commentResponseDTO.getContents()).isEqualTo(comment.getContents());
	}
	
	@Test
	@DisplayName("단일 댓글 조회 실패 - 게시글 없음")
	void 단일댓글조회실패_게시글없음() {
	    //given
		Long postId = 1L;
	    Long commentId = 1L;
		
		when(postService.getValidatePost(postId)).thenThrow(new PostException("게시글이 존재하지 않습니다."));
		
	    //when - then
		assertThatThrownBy(() -> commentService.getComment(postId, commentId))
			.isInstanceOf(PostException.class)
			.hasMessageContaining("게시글이 존재하지 않습니다.");
	}
	
	@Test
	@DisplayName("단일 댓글 조회 실패 - 댓글 없음")
	void 단일댓글조회실패_댓글없음() {
	    //given
	    Post post = createPost();
		Long commentId = 1L;
		
		when(postService.getValidatePost(post.getId())).thenReturn(post);
		when(commentRepository.findByPostIdAndId(post.getId(), commentId)).thenReturn(Optional.empty());
		
	    //when - then
		assertThatThrownBy(() -> commentService.getComment(post.getId(), commentId))
			.isInstanceOf(CommentException.class)
			.hasMessageContaining("게시글에 해당 댓글이 존재하지 않습니다.");
	}
	
	@Test
	@DisplayName("댓글 수정 성공")
	void 댓글수정성공() {
	    //given
	    User user = createUser();
		Post post = createPost();
		Comment comment = createComment();
		CommentRequestDTO requestDto = new CommentRequestDTO();
		requestDto.setContents("testContents");
		
		when(postService.getValidatePost(post.getId())).thenReturn(post);
		when(commentRepository.findByPostIdAndId(post.getId(), comment.getId())).thenReturn(Optional.of(comment));
		
	    //when
		CommentResponseDTO commentResponseDTO = commentService.updateComment(user, post.getId(), comment.getId(), requestDto);
	    
	    //then
		assertThat(commentResponseDTO.getContents()).isEqualTo(requestDto.getContents());
	}
	
	@Test
	@DisplayName("댓글 수정 실패 - 작성자 아님")
	void 댓글수정실패_작성자아님() {
	    //given
	    User user = createUser();
		User commentAuthor = User.builder()
			.userId("commentAuthor")
			.build();
		
		Post post = createPost();
		Comment comment = Comment.builder()
			.user(commentAuthor)
			.build();
		
		CommentRequestDTO requestDto = new CommentRequestDTO();
		requestDto.setContents("testContents");
		
		when(postService.getValidatePost(post.getId())).thenReturn(post);
		when(commentRepository.findByPostIdAndId(post.getId(), comment.getId())).thenReturn(Optional.of(comment));
		
	    //when - then
		assertThatThrownBy(() -> commentService.updateComment(user, post.getId(), comment.getId(), requestDto))
			.isInstanceOf(CommentException.class)
			.hasMessageContaining("작성자가 아니므로, 접근이 제한됩니다.");
	}
	
	@Test
	@DisplayName("댓글 삭제 성공")
	void 댓글삭제성공() {
	    //given
	    User user = createUser();
		Post post = createPost();
		Comment comment = createComment();
		
		when(postService.getValidatePost(post.getId())).thenReturn(post);
		when(commentRepository.findByPostIdAndId(post.getId(), comment.getId())).thenReturn(Optional.of(comment));
		
	    //when
		commentService.deleteComment(user, post.getId(), comment.getId());
	    
	    //then
		verify(commentRepository, times(1)).delete(comment);
	}
	
	@Test
	@DisplayName("댓글 삭제 실패 - 작성자 아님")
	void 댓글삭제실패_작성자아님() {
	    //given
		User user = createUser();
		User commentAuthor = User.builder()
			.userId("commentAuthor")
			.build();
		
		Post post = createPost();
		Comment comment = Comment.builder()
			.user(commentAuthor)
			.build();
		
		when(postService.getValidatePost(post.getId())).thenReturn(post);
		when(commentRepository.findByPostIdAndId(post.getId(), comment.getId())).thenReturn(Optional.of(comment));
		
	    //when - then
		assertThatThrownBy(() -> commentService.deleteComment(user, post.getId(), comment.getId()))
			.isInstanceOf(CommentException.class)
			.hasMessageContaining("작성자가 아니므로, 접근이 제한됩니다.");
	}
}
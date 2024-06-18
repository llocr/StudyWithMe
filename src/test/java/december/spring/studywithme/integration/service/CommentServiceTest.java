package december.spring.studywithme.integration.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import december.spring.studywithme.entity.Comment;
import december.spring.studywithme.entity.Post;
import december.spring.studywithme.entity.UserType;
import december.spring.studywithme.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import december.spring.studywithme.dto.CommentRequestDTO;
import december.spring.studywithme.dto.CommentResponseDTO;
import december.spring.studywithme.entity.User;
import december.spring.studywithme.repository.CommentRepository;
import december.spring.studywithme.repository.UserRepository;
import december.spring.studywithme.service.CommentService;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class CommentServiceTest {
	@Autowired
	CommentService commentService;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	static Long user1Id;
	static Long post1Id;
	static Long comment1Id;
	static Long comment2Id;
	static Long comment3Id;


	@BeforeEach
	void setUp() {
		User user1 = User.builder()
				.userId("test1")
				.password(passwordEncoder.encode("testtest12!"))
				.name("test1")
				.introduce("안녕하세요.")
				.email("1234@gmail.com")
				.userType(UserType.UNVERIFIED)
				.build();

		User saveUser = userRepository.save(user1);
		user1Id = saveUser.getId();

		Post post1 = Post.builder()
				.title("테스트 게시글입니다.")
				.contents("오늘의 스케줄은 없습니다.")
				.user(user1)
				.build();

		Post savaPost1 = postRepository.save(post1);
		post1Id = savaPost1.getId();

		Comment comment1 = Comment.builder()
				.contents("댓글 내용")
				.user(user1)
				.post(post1)
				.build();

		Comment comment2 = Comment.builder()
				.contents("댓글 내용2")
				.user(user1)
				.post(post1)
				.build();

		Comment comment3 = Comment.builder()
				.contents("댓글 내용3")
				.user(user1)
				.post(post1)
				.build();

		Comment saveComment1 = commentRepository.save(comment1);
		Comment saveComment2 = commentRepository.save(comment2);
		Comment saveComment3 = commentRepository.save(comment3);

		post1.getCommentList().add(saveComment1);
		post1.getCommentList().add(saveComment2);
		post1.getCommentList().add(saveComment3);

		postRepository.save(post1);

		comment1Id = saveComment1.getId();
		comment2Id = saveComment2.getId();
		comment3Id = saveComment3.getId();
	}
	
	@Test
	@DisplayName("댓글 등록 성공 테스트")
	void 댓글등록성공() {
	    //given
	    User user = userRepository.findById(user1Id).orElseThrow();
		Long postId = post1Id;
		CommentRequestDTO requestDTO = new CommentRequestDTO();
		requestDTO.setContents("댓글 내용");
		
	    //when
		CommentResponseDTO commentResponseDTO = commentService.createComment(user, postId, requestDTO);
		
		//then
		assertThat(commentResponseDTO.getContents()).isEqualTo(requestDTO.getContents());
		assertThat(commentResponseDTO.getUserId()).isEqualTo(user.getUserId());
		assertThat(commentResponseDTO.getPostId()).isEqualTo(postId);
	}
	
	@Test
	@DisplayName("전체 댓글 조회 성공 테스트")
	void 전체댓글조회성공() {
	    //given
	    Long postId = post1Id;
		
	    //when
		List<CommentResponseDTO> allComments = commentService.getAllComments(postId);
		
		//then
		assertThat(allComments).isNotNull();
		assertThat(allComments.size()).isEqualTo(3);
	}
	
	@Test
	@DisplayName("단일 댓글 조회 성공 테스트")
	void 단일댓글조회성공() {
	    //given
	    Long postId = post1Id;
		Long commentId = comment1Id;
		
	    //when
		CommentResponseDTO commentResponseDTO = commentService.getComment(postId, commentId);
	    
	    //then
		assertThat(commentResponseDTO).isNotNull();
		assertThat(commentResponseDTO.getPostId()).isEqualTo(postId);
	}
	
	@Test
	@DisplayName("댓글 수정 성공 테스트")
	void 댓글수정성공() {
	    //given
	    User user = userRepository.findById(user1Id).orElseThrow();
		Long postId = post1Id;
		Long commentId = comment1Id;
		CommentRequestDTO requestDTO = new CommentRequestDTO();
		requestDTO.setContents("수정된 댓글 내용");
		
	    //when
		CommentResponseDTO commentResponseDTO = commentService.updateComment(user, postId, commentId, requestDTO);
		
		//then
		assertThat(commentResponseDTO.getUserId()).isEqualTo(user.getUserId());
		assertThat(commentResponseDTO.getPostId()).isEqualTo(postId);
		assertThat(commentResponseDTO.getContents()).isEqualTo(requestDTO.getContents());
	}
	
	@Test
	@DisplayName("댓글 삭제 성공 테스트")
	void 댓글삭제성공() {
	    //given
	    User user = userRepository.findById(user1Id).orElseThrow();
		Long postId = post1Id;
		Long commentId = comment1Id;
		
	    //when
		commentService.deleteComment(user, postId, commentId);
	    
	    //then
		assertThat(commentRepository.findById(commentId)).isEmpty();
	}
	
	

}
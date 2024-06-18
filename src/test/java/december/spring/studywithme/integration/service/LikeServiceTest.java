package december.spring.studywithme.integration.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import december.spring.studywithme.entity.Comment;
import december.spring.studywithme.entity.Post;
import december.spring.studywithme.entity.UserType;
import december.spring.studywithme.repository.CommentRepository;
import december.spring.studywithme.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import december.spring.studywithme.entity.User;
import december.spring.studywithme.repository.UserRepository;
import december.spring.studywithme.service.LikeService;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class LikeServiceTest {
	@Autowired
	LikeService likeService;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	static Long user1Id;
	static Long user2Id;
	static Long post1Id;
	static Long comment1Id;

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

		User user2 = User.builder()
				.userId("test2")
				.password(passwordEncoder.encode("testtest12!"))
				.name("test2")
				.introduce("안녕하세요.")
				.email("12345@gmail.com")
				.userType(UserType.ACTIVE)
				.build();

		User saveUser1 = userRepository.save(user1);
		User saveUser2 = userRepository.save(user2);

		user1Id = saveUser1.getId();
		user2Id = saveUser2.getId();

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

		Comment saveComment1 = commentRepository.save(comment1);

		post1.getCommentList().add(saveComment1);

		postRepository.save(post1);

		comment1Id = saveComment1.getId();
	}
	
	@Test
	@DisplayName("게시글 좋아요 등록/취소 성공 테스트")
	void 게시글좋아요성공테스트() {
	    //given
	    Long postId = post1Id;
		User user = userRepository.findById(user2Id).orElseThrow();
		
	    //when
		boolean result = likeService.likePost(postId, user);
	    
	    //then
		assertThat(result).isTrue();
	}
	
	@Test
	@DisplayName("댓글 좋아요 등록/취소 성공 테스트")
	void 댓글좋아요성공테스트() {
	    //given
	    Long postId = post1Id;
		Long commentId = comment1Id;
		User user = userRepository.findById(user2Id).orElseThrow();
		
	    //when
		boolean result = likeService.likeComment(postId, commentId, user);
	    
	    //then
		assertThat(result).isTrue();
	}
}
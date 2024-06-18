package december.spring.studywithme.integration.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import december.spring.studywithme.entity.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import december.spring.studywithme.dto.PostPageResponseDTO;
import december.spring.studywithme.dto.PostRequestDTO;
import december.spring.studywithme.dto.PostResponseDTO;
import december.spring.studywithme.entity.Post;
import december.spring.studywithme.entity.User;
import december.spring.studywithme.repository.PostRepository;
import december.spring.studywithme.repository.UserRepository;
import december.spring.studywithme.service.PostService;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class PostServiceTest {
	@Autowired
	PostService postService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PostRepository postRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	static Long user1Id;
	static Long user2Id;
	static Long post1Id;
	static Long post2Id;
	static Long post3Id;

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

		Post post2 = Post.builder()
				.title("테스트 게시글입니다.")
				.contents("오늘의 스케줄은 없습니다.")
				.user(user1)
				.build();

		Post post3 = Post.builder()
				.title("테스트 게시글입니다.")
				.contents("오늘의 스케줄은 없습니다.")
				.user(user1)
				.build();

		Post savaPost1 = postRepository.save(post1);
		Post savePost2 = postRepository.save(post2);
		Post savePost3 = postRepository.save(post3);

		post1Id = savaPost1.getId();
		post2Id = savePost2.getId();
		post3Id = savePost3.getId();
	}
	
	@Test
	@DisplayName("게시글 생성 성공 테스트")
	void 게시글생성성공() {
	    //given
		PostRequestDTO postRequestDTO = new PostRequestDTO();
		postRequestDTO.setTitle("테스트입니다.");
		postRequestDTO.setContents("오늘의 스케줄은 없습니다.");

		User user = userRepository.findById(user1Id).orElseThrow();

		//when
		PostResponseDTO postResponseDTO = postService.createPost(user, postRequestDTO);
		
		//then
		assertThat(postResponseDTO.getUserId()).isEqualTo(user.getUserId());
		assertThat(postResponseDTO.getTitle()).isEqualTo(postRequestDTO.getTitle());
		assertThat(postResponseDTO.getContents()).isEqualTo(postRequestDTO.getContents());
	}
	
	@Test
	@DisplayName("게시글 단일 조회 성공 테스트")
	void 게시글단일조회성공() {
	    //given
	    Long id = post1Id;
		
	    //when
		PostResponseDTO postResponseDTO = postService.getPost(id);
	    
	    //then
		assertThat(postResponseDTO).isNotNull();
	}
	
	@Test
	@DisplayName("게시글 전체 조회 성공 테스트")        
	void 게시글전체조회성공() {
	    //given
	    Integer page = 1;
		String sortBy = "createdAt";
		String from = null;
		String to = null;
		
	    //when
		PostPageResponseDTO postPage = postService.getPostPage(page, sortBy, from, to);
		
		//then
		assertThat(postPage).isNotNull();
	}
	
	@Test
	@DisplayName("게시글 수정 성공 테스트")
	void 게시글수정성공() {
	    //given
	    Long id = post1Id;
		User user = userRepository.findById(user1Id).orElseThrow();
		PostRequestDTO postRequestDTO = new PostRequestDTO();
		postRequestDTO.setTitle("수정된 테스트입니다.");
		postRequestDTO.setContents("오늘의 스케줄은 무엇일까요?");
		
	    //when
		PostResponseDTO postResponseDTO = postService.updatePost(id, user, postRequestDTO);
	    
	    //then
		assertThat(postResponseDTO.getTitle()).isEqualTo(postRequestDTO.getTitle());
		assertThat(postResponseDTO.getContents()).isEqualTo(postRequestDTO.getContents());
	}
	
	@Test
	@DisplayName("게시글 삭제 성공 테스트")
	void 게시글삭제성공() {
	    //given
		Long id = post1Id;
		User user = userRepository.findById(user1Id).orElseThrow();
		
	    //when
		postService.deletePost(id, user);
		
		//then
		Optional<Post> deletedPost = postRepository.findById(id);
		assertThat(deletedPost).isEmpty();
	}

}
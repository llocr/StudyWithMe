package december.spring.studywithme.integration.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
	
	void createPost() {
		PostRequestDTO postRequestDTO = new PostRequestDTO();
		postRequestDTO.setTitle("테스트입니다.");
		postRequestDTO.setContents("오늘의 스케줄은 없습니다.");
		
		User user = userRepository.findById(1L).orElseThrow();
		
		postService.createPost(user, postRequestDTO);
	}
	
	@Test
	@DisplayName("게시글 생성 성공 테스트")
	void 게시글생성성공() {
	    //given
		PostRequestDTO postRequestDTO = new PostRequestDTO();
		postRequestDTO.setTitle("테스트입니다.");
		postRequestDTO.setContents("오늘의 스케줄은 없습니다.");
		
		User user = userRepository.findById(1L).orElseThrow();
		
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
	    Long id = 2L;
		
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
	    Long id = 2L;
		User user = userRepository.findById(1L).orElseThrow();
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
		Long id = 2L;
		User user = userRepository.findById(1L).orElseThrow();
		
	    //when
		postService.deletePost(id, user);
		
		//then
		Optional<Post> deletedPost = postRepository.findById(id);
		assertThat(deletedPost).isEmpty();
	}

}
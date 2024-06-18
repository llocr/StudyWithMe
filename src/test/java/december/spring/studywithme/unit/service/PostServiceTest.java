package december.spring.studywithme.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import december.spring.studywithme.dto.PostPageResponseDTO;
import december.spring.studywithme.dto.PostRequestDTO;
import december.spring.studywithme.dto.PostResponseDTO;
import december.spring.studywithme.entity.Post;
import december.spring.studywithme.entity.User;
import december.spring.studywithme.entity.UserType;
import december.spring.studywithme.exception.NoContentException;
import december.spring.studywithme.exception.PageException;
import december.spring.studywithme.exception.PostException;
import december.spring.studywithme.repository.PostRepository;
import december.spring.studywithme.service.PostService;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
	@Mock
	PostRepository postRepository;
	
	@InjectMocks
	PostService postService;
	
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
	
	public  Post createPost() {
		return Post.builder()
			.title("testTitle")
			.contents("testContents")
			.user(createUser())
			.build();
	}
	
	public PostRequestDTO createPostRequestDTO() {
		PostRequestDTO postRequestDTO = new PostRequestDTO();
		postRequestDTO.setTitle("testTitle");
		postRequestDTO.setContents("testContents");
		
		return postRequestDTO;
	}
	
	@Test
	@DisplayName("게시글 생성 성공")
	void 게시글생성성공() {
	    //given
		User user = createUser();
		PostRequestDTO postRequestDTO = createPostRequestDTO();
		Post post = Post.builder()
			.title(postRequestDTO.getTitle())
			.contents(postRequestDTO.getContents())
			.user(user)
			.build();
		
		when(postRepository.save(Mockito.any(Post.class))).thenReturn(post);
		
	    //when
		PostResponseDTO postResponseDTO = postService.createPost(user, postRequestDTO);
		
		//then
		assertThat(postResponseDTO).isNotNull();
		assertThat(postResponseDTO.getUserId()).isEqualTo(user.getUserId());
		assertThat(postResponseDTO.getTitle()).isEqualTo(postRequestDTO.getTitle());
		assertThat(postResponseDTO.getContents()).isEqualTo(postRequestDTO.getContents());
	}
	
	@Test
	@DisplayName("게시글 단일 조회 성공")
	void 게시글단일조회성공() {
	    //given
		Post post = createPost();
	    Long id = 1L;
		
		when(postRepository.findById(id)).thenReturn(Optional.of(post));
		
	    //when
		PostResponseDTO postResponseDTO = postService.getPost(id);
	    
	    //then
		assertThat(postResponseDTO).isNotNull();
		assertThat(postResponseDTO.getUserId()).isEqualTo(post.getUser().getUserId());
		assertThat(postResponseDTO.getTitle()).isEqualTo(post.getTitle());
		assertThat(postResponseDTO.getContents()).isEqualTo(post.getContents());
	}
	
	@Test
	@DisplayName("게시글 단일 조회 실패 - 존재하지 않는 게시글")
	void 게시글단일조회실패_존재하지않는게시글() {
	    //given
		Long id = 1L;
		
		when(postRepository.findById(id)).thenReturn(Optional.empty());
		
	    //when - then
		assertThatThrownBy(() -> postService.getPost(id))
			.isInstanceOf(PostException.class)
			.hasMessage("게시글이 존재하지 않습니다.");
	}
	
	@Test
	@DisplayName("전체 게시글 페이지 조회 성공 - 기간 시작/끝 설정")
	void 전체게시글조회성공_기간설정_시작끝() {
	    //given
		Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");
		Post post = createPost();
		Page<Post> postList = new PageImpl<>(List.of(post), pageable, 1);
		
		String from = "2023-01-01";
		String to = "2024-01-02";
		LocalDateTime startDate = LocalDateTime.parse(from + "T00:00:00").toLocalDate().atStartOfDay();
		LocalDateTime finishDate = LocalDateTime.parse(to + "T00:00:00").plusDays(1).toLocalDate().atStartOfDay();
		
		when(postRepository.findPostPageByPeriod(startDate, finishDate, pageable)).thenReturn(postList);
		
	    //when
		PostPageResponseDTO postPageResponseDTO = postService.getPostPage(1, "createdAt", from, to);
	    
	    //then
		assertThat(postPageResponseDTO).isNotNull();
		assertThat(postPageResponseDTO.getPostList()).hasSize(1);
	}
	
	@Test
	@DisplayName("전체 게시글 페이지 조회 성공 - 기간 시작만 설정")
	void 전체게시글조회성공_기간설정_시작() {
	    //given
		Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");
		Post post = createPost();
		Page<Post> postList = new PageImpl<>(List.of(post), pageable, 1);
		
		String from = "2023-01-01";
		LocalDateTime startDate = LocalDateTime.parse(from + "T00:00:00").toLocalDate().atStartOfDay();
	 
		when(postRepository.findPostPageByStartDate(startDate, pageable)).thenReturn(postList);
		
	    //when
		PostPageResponseDTO postPageResponseDTO = postService.getPostPage(1, "createdAt", from, null);
	    
	    //then
		assertThat(postPageResponseDTO).isNotNull();
		assertThat(postPageResponseDTO.getPostList()).hasSize(1);
	}
	
	@Test
	@DisplayName("전체 게시글 페이지 조회 성공 - 기간 끝만 설정")
	void 전체게시글조회성공_기간설정_끝() {
	    //given
		Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");
		Post post = createPost();
		Page<Post> postList = new PageImpl<>(List.of(post), pageable, 1);
		
		String to = "2024-01-02";
		LocalDateTime finishDate = LocalDateTime.parse(to + "T00:00:00").plusDays(1).toLocalDate().atStartOfDay();
		
		when(postRepository.findPostPageByFinishDate(finishDate, pageable)).thenReturn(postList);
		
	    //when
		PostPageResponseDTO postPageResponseDTO = postService.getPostPage(1, "createdAt", null, to);
		
	    //then
		assertThat(postPageResponseDTO).isNotNull();
		assertThat(postPageResponseDTO.getPostList()).hasSize(1);
	}
	
	@Test
	@DisplayName("전체 게시글 조회 성공 - 기간 설정 없음")
	void 전체게시글조회성공_기간설정_없음() {
	    //given
		Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");
		Post post = createPost();
		Page<Post> postList = new PageImpl<>(List.of(post), pageable, 1);
		
		when(postRepository.findAll(pageable)).thenReturn(postList);
		
		//when
		PostPageResponseDTO postPageResponseDTO = postService.getPostPage(1, "createdAt", null, null);
		
	    //then
		assertThat(postPageResponseDTO).isNotNull();
		assertThat(postPageResponseDTO.getPostList()).hasSize(1);
	}
	
	@Test
	@DisplayName("전체 게시글 조회 실패 - 기간 설정 오류")
	void 전체게시글조회실패_기간설정오류() {
	    //given
		Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");
		Post post = createPost();
		Page<Post> postList = new PageImpl<>(List.of(post), pageable, 1);
		
		String from = "2025-01-01";
		String to = "2024-01-02";
		
		//when - then
		assertThatThrownBy(() -> postService.getPostPage(1, "createdAt", from, to))
			.isInstanceOf(PageException.class)
			.hasMessage("기간 설정이 올바르지 않습니다.");
	}
	
	@Test
	@DisplayName("전체 게시글 조회 실패 - 날짜 형식 오류")
	void 전체게시글조회실패_날짜형식오류() {
	    //given
		Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");
		Post post = createPost();
		Page<Post> postList = new PageImpl<>(List.of(post), pageable, 1);
		
		String from = "20250101";
		
	    //when - then
		assertThatThrownBy(() -> postService.getPostPage(1, "createdAt", from, null))
			.isInstanceOf(PageException.class)
			.hasMessage("날짜 형식이 올바르지 않습니다. yyyy-mm-dd 형식으로 입력해주세요.");
	}
	
	@Test
	@DisplayName("전체 게시글 조회 실패 - 게시글 없음")
	void 전체게시글조회실패_게시글없음() {
	    //given
		Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");
		Page<Post> postList = new PageImpl<>(Collections.emptyList(), pageable, 0);
		
		when(postRepository.findAll(pageable)).thenReturn(postList);
		
	    //when - then
		assertThatThrownBy(() -> postService.getPostPage(1, "createdAt", null, null))
			.isInstanceOf(NoContentException.class)
			.hasMessage("게시글이 존재하지 않습니다.");
	}
	
	@Test
	@DisplayName("게시글 수정 성공")
	void 게시글수정성공() {
	    //given
	    User user = createUser();
		Post post = createPost();
		PostRequestDTO postRequestDTO = createPostRequestDTO();
		
		when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
		
	    //when
		PostResponseDTO postResponseDTO = postService.updatePost(post.getId(), user, postRequestDTO);
		
	    //then
		assertThat(postResponseDTO).isNotNull();
		assertThat(postResponseDTO.getUserId()).isEqualTo(user.getUserId());
		assertThat(postResponseDTO.getTitle()).isEqualTo(postRequestDTO.getTitle());
		assertThat(postResponseDTO.getContents()).isEqualTo(postRequestDTO.getContents());
	}
	
	@Test
	@DisplayName("게시글 수정 실패 - 게시글 없음")
	void 게시글수정실패_게시글없음() {
	    //given
		User user = createUser();
		Post post = createPost();
		PostRequestDTO postRequestDTO = createPostRequestDTO();
		
		when(postRepository.findById(post.getId())).thenReturn(Optional.empty());
	    
	    //when - then
		assertThatThrownBy(() -> postService.updatePost(post.getId(), user, postRequestDTO))
			.isInstanceOf(PostException.class)
			.hasMessage("게시글이 존재하지 않습니다.");
	}
	
	@Test
	@DisplayName("게시글 수정 실패 - 작성자가 아님")
	void 게시글수정실패_작성자아님() {
	    //given
		User user = User.builder()
			.userId("anotherId")
			.build();
		
		Post post = createPost();
		PostRequestDTO postRequestDTO = createPostRequestDTO();
		
		when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
	    
	    //when
		assertThatThrownBy(() -> postService.updatePost(post.getId(), user, postRequestDTO))
			.isInstanceOf(PostException.class)
			.hasMessage("작성자가 아니므로, 접근이 제한됩니다.");
	}
	
	@Test
	@DisplayName("게시글 삭제 성공")
	void 게시글삭제성공() {
	    //given
		Long id = 1L;
		User user = createUser();
		Post post = createPost();
		
		when(postRepository.findById(id)).thenReturn(Optional.of(post));
		
	    //when
		postService.deletePost(id, user);
	    
	    //then
		verify(postRepository, times(1)).findById(1L);
		verify(postRepository, times(1)).delete(post);
	}
	
	@Test
	@DisplayName("게시글 삭제 실패 - 게시글 없음")
	void 게시글삭제실패_게시글없음() {
	    //given
	    Long id = 1L;
		User user = createUser();
		when(postRepository.findById(id)).thenReturn(Optional.empty());
		
	    //when - then
		assertThatThrownBy(() -> postService.deletePost(id, user))
			.isInstanceOf(PostException.class)
			.hasMessage("게시글이 존재하지 않습니다.");
	}
	
	@Test
	@DisplayName("게시글 삭제 실패 - 작성자가 아님")
	void 게시글삭제실패_작성자아님() {
	    //given
	    User user = User.builder()
			.userId("anotherId")
			.build();
		
		Post post = createPost();
		
		when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
		
	    //when - then
		assertThatThrownBy(() -> postService.deletePost(post.getId(), user))
			.isInstanceOf(PostException.class)
			.hasMessage("작성자가 아니므로, 접근이 제한됩니다.");
	}
	
}
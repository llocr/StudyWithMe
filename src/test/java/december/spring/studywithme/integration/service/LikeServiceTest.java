package december.spring.studywithme.integration.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
	
	@Test
	@DisplayName("게시글 좋아요 등록/취소 성공 테스트")
	void 게시글좋아요성공테스트() {
	    //given
	    Long postId = 2L;
		User user = userRepository.findById(2L).orElseThrow();
		
	    //when
		boolean result = likeService.likePost(postId, user);
	    
	    //then
		assertThat(result).isTrue();
	}
	
	@Test
	@DisplayName("댓글 좋아요 등록/취소 성공 테스트")
	void 댓글좋아요성공테스트() {
	    //given
	    Long postId = 2L;
		Long commentId = 1L;
		User user = userRepository.findById(2L).orElseThrow();
		
	    //when
		boolean result = likeService.likeComment(postId, commentId, user);
	    
	    //then
		assertThat(result).isTrue();
	}
}
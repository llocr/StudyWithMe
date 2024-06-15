package december.spring.studywithme.integration.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
	
	void createComment() {
		//given
		User user = userRepository.findById(1L).orElseThrow();
		Long postId = 2L;
		CommentRequestDTO requestDTO = new CommentRequestDTO();
		requestDTO.setContents("댓글 내용");
		
		commentService.createComment(user, postId, requestDTO);
		commentService.createComment(user, postId, requestDTO);
		commentService.createComment(user, postId, requestDTO);
		commentService.createComment(user, postId, requestDTO);
	}
	
	@Test
	@DisplayName("댓글 등록 성공 테스트")
	void 댓글등록성공() {
	    //given
	    User user = userRepository.findById(1L).orElseThrow();
		Long postId = 2L;
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
	    Long postId = 2L;
		
	    //when
		List<CommentResponseDTO> allComments = commentService.getAllComments(postId);
		
		//then
		assertThat(allComments).isNotNull();
		assertThat(allComments.size()).isEqualTo(4);
	}
	
	@Test
	@DisplayName("단일 댓글 조회 성공 테스트")
	void 단일댓글조회성공() {
	    //given
	    Long postId = 2L;
		Long commentId = 1L;
		
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
	    User user = userRepository.findById(1L).orElseThrow();
		Long postId = 2L;
		Long commentId = 1L;
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
	    User user = userRepository.findById(1L).orElseThrow();
		Long postId = 2L;
		Long commentId = 1L;
		
	    //when
		commentService.deleteComment(user, postId, commentId);
	    
	    //then
		assertThat(commentRepository.findById(commentId)).isEmpty();
	}
	
	

}
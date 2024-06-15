package december.spring.studywithme.unit.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import december.spring.studywithme.config.SecurityConfig;
import december.spring.studywithme.controller.CommentController;
import december.spring.studywithme.dto.CommentRequestDTO;
import december.spring.studywithme.entity.User;
import december.spring.studywithme.entity.UserType;
import december.spring.studywithme.filter.MockSpringSecurityFilter;
import december.spring.studywithme.security.UserDetailsImpl;
import december.spring.studywithme.service.CommentService;

@WebMvcTest(
	controllers = {CommentController.class},
	excludeFilters = {
		@ComponentScan.Filter(
			type = FilterType.ASSIGNABLE_TYPE,
			classes = SecurityConfig.class
		)
	}
)
class CommentControllerTest {
	private MockMvc mvc;
	
	private Principal principal;
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	CommentService commentService;
	
	@BeforeEach
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context)
			.apply(springSecurity(new MockSpringSecurityFilter()))
			.build();
	}
	
	private void mockUserSetup() {
		User user = User.builder()
			.userId("testId1212121212")
			.password("testPassword!1212")
			.name("testName")
			.email("123@gmail.com")
			.introduce("testIntroduce")
			.userType(UserType.DEACTIVATED)
			.build();
		
		UserDetailsImpl userDetails = new UserDetailsImpl(user);
		principal = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}
	
	@Test
	@DisplayName("댓글 등록 성공 테스트")
	void 댓글등록성공() throws Exception {
	    //given
	    this.mockUserSetup();
		Long id = 1L;
		CommentRequestDTO requestDto = new CommentRequestDTO();
		requestDto.setContents("test content");
		String postInfo = objectMapper.writeValueAsString(requestDto);
		
	    //when
		ResultActions resultActions = mvc.perform(post("/api/posts/{postId}/comments", id)
			.content(postInfo)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.principal(principal)
		);
		
		//then
		resultActions.andExpect(status().isCreated())
			.andDo(print());
	}
	
	@Test
	@DisplayName("댓글 등록 실패 테스트 - Validation")
	void 댓글등록실패() throws Exception {
	    //given
		this.mockUserSetup();
		Long id = 1L;
		String postInfo = "";
		
		//when
		ResultActions resultActions = mvc.perform(post("/api/posts/{postId}/comments", id)
			.content(postInfo)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.principal(principal)
		);
		
		//then
		resultActions.andExpect(status().is4xxClientError())
			.andDo(print());
	}
	
	@Test
	@DisplayName("전체 댓글 조회 성공 테스트")
	void 전체댓글조회성공() throws Exception {
	    //given
	    Long id = 1L;
		
	    //when
		ResultActions resultActions = mvc.perform(get("/api/posts/{postId}/comments", id));
		
		//then
		resultActions.andExpect(status().isOk())
			.andDo(print());
	}
	
	@Test
	@DisplayName("단일 댓글 조회 성공 테스트")
	void 단일댓글조회성공() throws Exception {
	    //given
	    Long postId = 1L;
		Long commentId = 1L;
		
	    //when
		ResultActions resultActions = mvc.perform(get("/api/posts/{postId}/comments/{commentId}", postId, commentId));
	    
	    //then
		resultActions.andExpect(status().isOk())
	        .andDo(print());
	}
	
	@Test
	@DisplayName("댓글 수정 성공 테스트")
	void 댓글수정성공() throws Exception {
	    //given
	    this.mockUserSetup();
		Long postId = 1L;
		Long commentId = 1L;
		
		CommentRequestDTO requestDto = new CommentRequestDTO();
		requestDto.setContents("test content");
		String postInfo = objectMapper.writeValueAsString(requestDto);
		
		//when
		ResultActions resultActions = mvc.perform(put("/api/posts/{postId}/comments/{commentId}", postId, commentId)
			.content(postInfo)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.principal(principal)
		);
		
		//then
		resultActions.andExpect(status().isOk())
			.andDo(print());
	}
	
	@Test
	@DisplayName("댓글 수정 실패 테스트 - Validation")
	void 댓글수정실패() throws Exception {
	    //given
		this.mockUserSetup();
		Long postId = 1L;
		Long commentId = 1L;
		String postInfo = "";
	    
	    //when
		ResultActions resultActions = mvc.perform(put("/api/posts/{postId}/comments/{commentId}", postId, commentId)
			.content(postInfo)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.principal(principal)
		);
		
	    //then
		resultActions.andExpect(status().is4xxClientError())
			.andDo(print());
	}
	
	@Test
	@DisplayName("댓글 삭제 성공 테스트")
	void 댓글삭제성공() throws Exception {
	    //given
	    this.mockUserSetup();
		Long postId = 1L;
		Long commentId = 1L;
		
	    //when
		ResultActions resultActions = mvc.perform(delete("/api/posts/{postId}/comments/{commentId}", postId, commentId)
			.principal(principal)
		);
		
		//then
		resultActions.andExpect(status().isOk())
			.andDo(print());
	}
}
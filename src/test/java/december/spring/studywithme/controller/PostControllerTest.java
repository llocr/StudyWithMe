package december.spring.studywithme.controller;

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
import december.spring.studywithme.dto.PostRequestDTO;
import december.spring.studywithme.entity.User;
import december.spring.studywithme.entity.UserType;
import december.spring.studywithme.filter.MockSpringSecurityFilter;
import december.spring.studywithme.security.UserDetailsImpl;
import december.spring.studywithme.service.PostService;

@WebMvcTest(
	controllers = {PostController.class},
	excludeFilters = {
		@ComponentScan.Filter(
			type = FilterType.ASSIGNABLE_TYPE,
			classes = SecurityConfig.class
		)
	}
)
class PostControllerTest {
	private MockMvc mvc;
	
	private Principal principal;
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	PostService postService;
	
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
	@DisplayName("게시글 등록 성공")
	void 게시글등록성공() throws Exception {
	    //given
	    this.mockUserSetup();
		PostRequestDTO postRequestDTO = new PostRequestDTO();
		postRequestDTO.setTitle("testTitle");
		postRequestDTO.setContents("testContent");
		
		String postInfo = objectMapper.writeValueAsString(postRequestDTO);
		
		//when
		ResultActions resultActions = mvc.perform(post("/api/posts")
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
	@DisplayName("게시글 등록 실패 - Validation")
	void 게시글등록실패() throws Exception {
	    //given
		this.mockUserSetup();
	    String postInfo = "";
		
	    //when
		ResultActions resultActions = mvc.perform(post("/api/posts")
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
	@DisplayName("단일 게시글 조회")
	void 단일게시글조회() throws Exception {
	    //given
	    Long id = 1L;
		
	    //when
		ResultActions resultActions = mvc.perform(get("/api/posts/" + id));
		
		//then
		resultActions.andExpect(status().isOk())
			.andDo(print());
	}
	
	@Test
	@DisplayName("전체 게시글 조회")
	void 전체게시글조회() throws Exception {
	    //when
		ResultActions resultActions = mvc.perform(get("/api/posts")
			.param("page", "1")
			.param("sortBy", "createdAt")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
		);
		
		//then
		resultActions.andExpect(status().isOk())
			.andDo(print());
	}
	
	@Test
	@DisplayName("게시글 수정 성공")
	void 게시글수정성공() throws Exception {
	    //given
	    this.mockUserSetup();
		
		PostRequestDTO postRequestDTO = new PostRequestDTO();
		postRequestDTO.setTitle("testTitle");
		postRequestDTO.setContents("testContent");
		String postInfo = objectMapper.writeValueAsString(postRequestDTO);
		
		Long id = 1L;
		
	    //when
		ResultActions resultActions = mvc.perform(put("/api/posts/" + id)
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
	@DisplayName("게시글 수정 실패 - Validation")
	void 게시글수정실패() throws Exception {
	    //given
		this.mockUserSetup();
		String postInfo = "";
		Long id = 1L;
	    
	    //when
		ResultActions resultActions = mvc.perform(put("/api/posts/" + id)
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
	@DisplayName("게시글 삭제 성공")
	void 게시글삭제성공() throws Exception {
	    //given
	    this.mockUserSetup();
		Long id = 1L;
		
	    //when
		ResultActions resultActions = mvc.perform(delete("/api/posts/" + id)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.principal(principal)
		);
		
		//then
		resultActions.andExpect(status().isOk())
			.andDo(print());
	}
}
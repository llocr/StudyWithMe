package december.spring.studywithme.unit.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import december.spring.studywithme.config.SecurityConfig;
import december.spring.studywithme.controller.LikeController;
import december.spring.studywithme.entity.User;
import december.spring.studywithme.entity.UserType;
import december.spring.studywithme.filter.MockSpringSecurityFilter;
import december.spring.studywithme.security.UserDetailsImpl;
import december.spring.studywithme.service.LikeService;

@WebMvcTest(
	controllers = {LikeController.class},
	excludeFilters = {
		@ComponentScan.Filter(
			type = FilterType.ASSIGNABLE_TYPE,
			classes = SecurityConfig.class
		)
	}
)
class LikeControllerTest {
	private MockMvc mvc;
	
	private Principal principal;
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	LikeService likeService;
	
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
	@DisplayName("게시글 좋아요 등록/취소")
	void 게시글좋아요등록취소() throws Exception {
	    //given
	    this.mockUserSetup();
		Long id = 1L;
		
	    //when
		ResultActions resultActions = mvc.perform(post("/api/posts/{id}/like", id)
			.principal(principal)
		);
	    
	    //then
		resultActions.andExpect(status().isOk())
			.andDo(print());
	}
	
	@Test
	@DisplayName("댓글 좋아요 등록/취소")
	void 댓글좋아요등록취소() throws Exception {
	    //given
	    this.mockUserSetup();
		Long postId = 1L;
		Long commentId = 1L;
		
	    //when
		ResultActions resultActions = mvc.perform(post("/api/posts/{postId}/comments/{commentId}/like", postId, commentId)
			.principal(principal)
		);
		
		//then
		resultActions.andExpect(status().isOk())
			.andDo(print());
	}
	
	
}
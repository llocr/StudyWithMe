package december.spring.studywithme.unit.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
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
import december.spring.studywithme.controller.UserController;
import december.spring.studywithme.dto.EditPasswordRequestDTO;
import december.spring.studywithme.dto.PasswordRequestDTO;
import december.spring.studywithme.dto.UserProfileRequestDTO;
import december.spring.studywithme.dto.UserRequestDTO;
import december.spring.studywithme.entity.User;
import december.spring.studywithme.entity.UserType;
import december.spring.studywithme.filter.MockSpringSecurityFilter;
import december.spring.studywithme.jwt.JwtUtil;
import december.spring.studywithme.security.UserDetailsImpl;
import december.spring.studywithme.service.UserService;
import december.spring.studywithme.utils.MonkeyUtils;

@WebMvcTest(
	controllers = {UserController.class},
	excludeFilters = {
		@ComponentScan.Filter(
			type = FilterType.ASSIGNABLE_TYPE,
			classes = SecurityConfig.class
		)
	}
)
class UserControllerTest {
	private MockMvc mvc;
	
	private Principal principal;
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	UserService userService;
	
	@MockBean
	JwtUtil jwtUtil;
	
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
	@DisplayName("회원가입 성공")
	void 회원가입성공() throws Exception {
		//given
		UserRequestDTO userRequestDTO = new UserRequestDTO();
		userRequestDTO.setUserId("testId12121212");
		userRequestDTO.setPassword("testPassword!1212");
		userRequestDTO.setName("testName");
		userRequestDTO.setEmail("test@test.com");
		userRequestDTO.setIntroduce("testIntroduce");
		
		String postInfo = objectMapper.writeValueAsString(userRequestDTO);
		
		//when
		ResultActions resultActions = mvc.perform(post("/api/users/signup")
			.content(postInfo)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
		);
		
		//then
		resultActions.andExpect(status().isCreated())
			.andDo(print());
	}
	
	@Test
	@DisplayName("회원가입 실패 - Validation")
	void 회원가입실패() throws Exception {
		//given
		UserRequestDTO userRequestDTO = MonkeyUtils.commonMonkey().giveMeOne(UserRequestDTO.class);
		
		String postInfo = objectMapper.writeValueAsString(userRequestDTO);
		
		//when
		ResultActions resultActions = mvc.perform(post("/api/users/signup")
			.content(postInfo)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
		);
		
		//then
		resultActions.andExpect(status().is4xxClientError())
			.andDo(print());
	}
	
	@Test
	@DisplayName("회원탈퇴 성공")
	void 회원탈퇴성공() throws Exception {
		//given
		this.mockUserSetup();
		PasswordRequestDTO passwordRequestDTO = new PasswordRequestDTO();
		passwordRequestDTO.setPassword("testPassword!1212");
		
		String postInfo = objectMapper.writeValueAsString(passwordRequestDTO);
		
		//when
		ResultActions resultActions = mvc.perform(put("/api/users/withdraw")
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
	@DisplayName("회원탈퇴 실패 - Validation")
	void 회원탈퇴실패() throws Exception {
	    //given
		this.mockUserSetup();
	    PasswordRequestDTO passwordRequestDTO = new PasswordRequestDTO();
		String postInfo = objectMapper.writeValueAsString(passwordRequestDTO);
		
	    //when
		ResultActions resultActions = mvc.perform(put("/api/users/withdraw")
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
	@DisplayName("로그아웃 성공")
	void 로그아웃성공() throws Exception {
	    //given
	    this.mockUserSetup();
		
	    //when - then
		ResultActions resultActions = mvc.perform(get("/api/users/logout")
			.principal(principal)
		);
		
		//then
		resultActions.andExpect(status().isOk())
			.andDo(print());
	}
	
	@Test
	@DisplayName("회원 정보 조회 성공")
	void 회원정보조회성공() throws Exception {
	    //given
	    Long id = 1L;
		
	    //when
		ResultActions resultActions = mvc.perform(get("/api/users/" + id));
		
		//then
		resultActions.andExpect(status().isOk())
			.andDo(print());
	}
	
	@Test
	@DisplayName("로그인한 사용자 프로필 조회")
	void 사용자프로필조회() throws Exception {
	    //given
	    this.mockUserSetup();
		
	    //when
		ResultActions resultActions = mvc.perform(get("/api/users/mypage")
			.principal(principal)
		);
		
		//then
		resultActions.andExpect(status().isOk())
			.andDo(print());
	}
	
	@Test
	@DisplayName("로그인한 사용자 프로필 수정")
	void 사용자프로필수성공() throws Exception {
	    //given
		this.mockUserSetup();
		UserProfileRequestDTO userProfileRequestDTO = new UserProfileRequestDTO();
		userProfileRequestDTO.setUserId("testId12121212");
		userProfileRequestDTO.setName("testName");
		userProfileRequestDTO.setIntroduce("testIntroduce");
		userProfileRequestDTO.setCurrentPassword("testPassword!1212");
		
		String postInfo = objectMapper.writeValueAsString(userProfileRequestDTO);
		
		//when
		ResultActions resultActions = mvc.perform(put("/api/users/mypage")
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
	@DisplayName("로그인한 사용자 비밀번호 수정 성공")
	void 사용자비밀번호수정성공() throws Exception {
	    //given
	    this.mockUserSetup();
		EditPasswordRequestDTO editPasswordRequestDTO = new EditPasswordRequestDTO();
		editPasswordRequestDTO.setCurrentPassword("testPassword!1212");
		editPasswordRequestDTO.setNewPassword("testPassword!12");
		String postInfo = objectMapper.writeValueAsString(editPasswordRequestDTO);
		
		//when
		ResultActions resultActions = mvc.perform(put("/api/users/password")
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
	@DisplayName("로그인한 사용자 비밀번호 수정 실패 - Validation")
	void 사용자비밀번호수정실패() throws Exception {
	    //given
		this.mockUserSetup();
		String postInfo = "";
		
		//when
		ResultActions resultActions = mvc.perform(put("/api/users/password")
			.content(postInfo)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.principal(principal)
		);
		
		//then
		resultActions.andExpect(status().is4xxClientError())
			.andDo(print());
	}
}
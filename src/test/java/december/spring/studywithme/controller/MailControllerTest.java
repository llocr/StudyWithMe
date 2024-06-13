package december.spring.studywithme.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import december.spring.studywithme.config.SecurityConfig;
import december.spring.studywithme.dto.CertificationNumberRequestDTO;
import december.spring.studywithme.entity.User;
import december.spring.studywithme.entity.UserType;
import december.spring.studywithme.filter.MockSpringSecurityFilter;
import december.spring.studywithme.security.UserDetailsImpl;
import december.spring.studywithme.service.MailService;
import december.spring.studywithme.service.UserService;

@WebMvcTest(
	controllers = {MailController.class},
	excludeFilters = {
		@ComponentScan.Filter(
			type = FilterType.ASSIGNABLE_TYPE,
			classes = SecurityConfig.class
		)
	}
)
class MailControllerTest {
	private MockMvc mvc;
	
	private Principal principal;
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	MailService mailService;
	
	@MockBean
	UserService userService;
	
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
	@DisplayName("이메일 인증 코드 발송")
	void 이메일인증코드발송() throws Exception {
	    //given
	    this.mockUserSetup();
		
	    //when
		ResultActions resultActions = mvc.perform(post("/api/mails")
			.principal(principal)
		);
		
		//then
		resultActions.andExpect(status().isOk())
			.andDo(print());
	}
	
	@Test
	@DisplayName("이메일 인증 코드 확인 성공")
	void 이메일인증코드확인성공() throws Exception {
	    //given
	    this.mockUserSetup();
		CertificationNumberRequestDTO certificationNumberRequestDTO = new CertificationNumberRequestDTO();
		certificationNumberRequestDTO.setCode("123456");
		String postInfo = objectMapper.writeValueAsString(certificationNumberRequestDTO);
		
		//when
		ResultActions resultActions = mvc.perform(get("/api/mails")
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
	@DisplayName("이메일 인증 코드 확인 실패 - Validation")
	void 이메일인증코드확인실패() throws Exception {
	    //given
	    this.mockUserSetup();
		String postInfo = "";
		
	    //when
		ResultActions resultActions = mvc.perform(get("/api/mails")
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
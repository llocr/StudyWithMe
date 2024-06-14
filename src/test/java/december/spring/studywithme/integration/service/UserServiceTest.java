package december.spring.studywithme.integration.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import december.spring.studywithme.dto.EditPasswordRequestDTO;
import december.spring.studywithme.dto.PasswordRequestDTO;
import december.spring.studywithme.dto.UserProfileRequestDTO;
import december.spring.studywithme.dto.UserProfileResponseDTO;
import december.spring.studywithme.dto.UserRequestDTO;
import december.spring.studywithme.dto.UserResponseDTO;
import december.spring.studywithme.entity.User;
import december.spring.studywithme.entity.UserType;
import december.spring.studywithme.exception.UserException;
import december.spring.studywithme.repository.UserRepository;
import december.spring.studywithme.service.UserService;
import december.spring.studywithme.utils.MonkeyUtils;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UserServiceTest {
	@Autowired
	UserService userService;
	
	@Autowired
	private UserRepository userRepository;
	
	void createUsers() {
		UserRequestDTO user = new UserRequestDTO();
		user.setUserId("test1212");
		user.setName("test");
		user.setPassword("test");
		user.setEmail("test1212@gmail.com");
		user.setIntroduce("test");
		userService.createUser(user);
	}
	
	@Test
	@DisplayName("유저 생성 성공 테스트")
	void 유저생성성공() {
	    //given
		UserRequestDTO requestDTO = new UserRequestDTO();
		requestDTO.setUserId("heesue");
		requestDTO.setName("test");
		requestDTO.setPassword("test");
		requestDTO.setEmail("12123@gmail.com");
		requestDTO.setIntroduce("test");
		
	    //when
		UserResponseDTO responseDTO = userService.createUser(requestDTO);
		
		//then
		assertThat(responseDTO.getUserId()).isEqualTo(requestDTO.getUserId());
		assertThat(responseDTO.getName()).isEqualTo(requestDTO.getName());
		assertThat(responseDTO.getEmail()).isEqualTo(requestDTO.getEmail());
		assertThat(responseDTO.getIntroduce()).isEqualTo(requestDTO.getIntroduce());
	}
	
	@Test
	@DisplayName("유저 생성 실패 테스트 - 중복된 아이디")
	void 유저생성실패_중복된아이디() {
	    //given
	    UserRequestDTO requestDTO = MonkeyUtils.commonMonkey().giveMeOne(UserRequestDTO.class);
		requestDTO.setUserId("test");
		
	    //when
	    assertThatThrownBy(() -> userService.createUser(requestDTO))
			.isInstanceOf(UserException.class);
	}
	
	
	@Test
	@DisplayName("유저 생성 실패 테스트 - 중복된 이메일")
	void 유저생성실패_중복된이메일() {
	    //given
		UserRequestDTO requestDTO = MonkeyUtils.commonMonkey().giveMeOne(UserRequestDTO.class);
		requestDTO.setEmail("test@gmail.com");
		
	    //when - then
		assertThatThrownBy(() -> userService.createUser(requestDTO))
			.isInstanceOf(UserException.class);
	}
	
	@Test
	@DisplayName("유저 활성화 테스트")
	void 유저활성화() {
	    //given
		User user = userRepository.findById(1L).orElseThrow();
		
	    //when
		userService.updateUserActive(user);
	    
	    //then
		assertThat(user.getUserType()).isEqualTo(UserType.ACTIVE);
	}
	
	@Test
	@DisplayName("유저 탈퇴 성공")
	void 유저탈퇴성공() {
	    //given
		User user = userRepository.findById(4L).orElseThrow();
		PasswordRequestDTO requestDTO = new PasswordRequestDTO();
		requestDTO.setPassword("test");
		
	    //when
		String userId = userService.withdrawUser(requestDTO, user);
		
		//then
		assertThat(userId).isEqualTo(user.getUserId());
		assertThat(user.getUserType()).isEqualTo(UserType.DEACTIVATED);
	}
	
	@Test
	@DisplayName("유저 탈퇴 실패 - 이미 탈퇴된 회원")
	void 유저탈퇴실패_이미탈퇴된회원() {
	    //given
	    User user = userRepository.findById(2L).orElseThrow();
		PasswordRequestDTO requestDTO = new PasswordRequestDTO();
		requestDTO.setPassword("test");
		
	    //when - then
		assertThatThrownBy(() -> userService.withdrawUser(requestDTO, user)
			).isInstanceOf(UserException.class);
	}
	
	@Test
	@DisplayName("유저 탈퇴 실패 - 비밀번호 불일치")
	void 유저탈퇴실패_비밀번호불일치() {
	    //given
		User user = userRepository.findById(1L).orElseThrow();
		PasswordRequestDTO requestDTO = new PasswordRequestDTO();
		requestDTO.setPassword("test");
		
	    //when - then
		assertThatThrownBy(() -> userService.withdrawUser(requestDTO, user)
			).isInstanceOf(UserException.class);
	}
	
	@Test
	@DisplayName("로그아웃 성공")
	void 로그아웃성공() {
	    //given
	    User user = userRepository.findById(1L).orElseThrow();
		String accessToken = "accessToken";
		String refreshToken = "refreshToken";
		
	    //when
		userService.logout(user, accessToken, refreshToken);
	    
	    //then
		assertThat(user.getRefreshToken()).isEqualTo("");
	}
	
	@Test
	@DisplayName("로그아웃 실패 - 로그인 되어 있지 않은 유저")
	void 로그아웃실패_로그인되지않은유저() {
	    //given
	    User user = null;
		String accessToken = "accessToken";
		String refreshToken = "refreshToken";
		
	    //when - then
		assertThatThrownBy(() -> userService.logout(user, accessToken, refreshToken)
			).isInstanceOf(UserException.class);
	}
	
	@Test
	@DisplayName("로그아웃 실패 - 이미 탈퇴한 회원")
	void 로그아웃실패_이미탈퇴한회원() {
	    //given
	    User user = userRepository.findById(2L).orElseThrow();
		String accessToken = "accessToken";
		String refreshToken = "refreshToken";
		
		//when - then
		assertThatThrownBy(() -> userService.logout(user, accessToken, refreshToken)
		).isInstanceOf(UserException.class);
	}
	
	@Test
	@DisplayName("로그인한 유저 조회")
	void 로그인한유저조회() {
	    //given
	    User user = userRepository.findById(2L).orElseThrow();
		
	    //when
		UserProfileResponseDTO userProfileResponseDTO = userService.inquiryUser(user);
		
		//then
		assertThat(userProfileResponseDTO.getUserId()).isEqualTo(user.getUserId());
		assertThat(userProfileResponseDTO.getEmail()).isEqualTo(user.getEmail());
		assertThat(userProfileResponseDTO.getIntroduce()).isEqualTo(user.getIntroduce());
	}
	
	@Test
	@DisplayName("유저 조회 성공")
	void 유저조회성공() {
	    //given
	    Long Id = 2L;
		
	    //when
		UserResponseDTO userResponseDTO = userService.inquiryUserById(Id);
	    
	    //then
		assertThat(userResponseDTO.getUserId()).isEqualTo("test");
		assertThat(userResponseDTO.getName()).isEqualTo("test");
	}
	
	@Test
	@DisplayName("유저 조회 실패 - 존재하지 않는 유저")
	void 유저조회실패_존재하지않는유저() {
	    //given
		Long id = 99L;
	    
	    //when - then
		assertThatThrownBy(() -> userService.inquiryUserById(id)
			).isInstanceOf(UserException.class);
	}
	
	@Test
	@DisplayName("유저 프로필 수정 성공")
	void 유저프로필수정성공() {
	    //given
		UserProfileRequestDTO requestDTO = MonkeyUtils.commonMonkey().giveMeOne(UserProfileRequestDTO.class);
		requestDTO.setCurrentPassword("test");
		User user = userRepository.findById(2L).orElseThrow();
		
	    //when
		UserResponseDTO userResponseDTO = userService.editProfile(requestDTO, user);
	    
	    //then
		assertThat(userResponseDTO.getUserId()).isEqualTo(user.getUserId());
	}
	
	@Test
	@DisplayName("유저 프로필 수정 실패 - 비밀번호 불일치")
	void 유저프로필수정실패_비밀번호불일치() {
	    //given
		UserProfileRequestDTO requestDTO = MonkeyUtils.commonMonkey().giveMeOne(UserProfileRequestDTO.class);
		requestDTO.setCurrentPassword("test1");
		User user = userRepository.findById(2L).orElseThrow();
	    
	    //when - then
		assertThatThrownBy(() -> userService.editProfile(requestDTO, user)
			).isInstanceOf(UserException.class);
	}
	
	@Test
	@DisplayName("비밀번호 변경 성공")
	void 비밀번호변경성공() {
	    //given
		EditPasswordRequestDTO requestDTO = new EditPasswordRequestDTO();
		requestDTO.setCurrentPassword("test");
		requestDTO.setNewPassword("test1");
		
		User user = userRepository.findById(2L).orElseThrow();
		
	    //when
		UserResponseDTO userResponseDTO = userService.editPassword(requestDTO, user);
	    
	    //then
		assertThat(userResponseDTO.getUserId()).isEqualTo(user.getUserId());
	}
	
	@Test
	@DisplayName("비밀번호 변경 실패 - 비밀번호 불일치")
	void 비밀번호변경실패_비밀번호불일치() {
	    //given
		EditPasswordRequestDTO requestDTO = new EditPasswordRequestDTO();
		requestDTO.setCurrentPassword("test1");
		requestDTO.setNewPassword("test2");
	 
		User user = userRepository.findById(2L).orElseThrow();
		
	    //when - then
		assertThatThrownBy(() -> userService.editPassword(requestDTO, user))
			.isInstanceOf(UserException.class);
	}
	
	@Test
	@DisplayName("비밀번호 변경 실패 - 새로운 비밀번호와 기존 비밀번호 동일")
	void 비밀번호변경실패_새로운비밀번호와기존비밀번호동일() {
	    //given
		EditPasswordRequestDTO requestDTO = new EditPasswordRequestDTO();
		requestDTO.setCurrentPassword("test");
		requestDTO.setNewPassword("test");
		
		User user = userRepository.findById(2L).orElseThrow();
		
	    //when - then
		assertThatThrownBy(() -> userService.editPassword(requestDTO, user)
			).isInstanceOf(UserException.class);
	}
}
package december.spring.studywithme.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import december.spring.studywithme.dto.EditPasswordRequestDTO;
import december.spring.studywithme.dto.PasswordRequestDTO;
import december.spring.studywithme.dto.UserProfileRequestDTO;
import december.spring.studywithme.dto.UserProfileResponseDTO;
import december.spring.studywithme.dto.UserRequestDTO;
import december.spring.studywithme.dto.UserResponseDTO;
import december.spring.studywithme.entity.User;
import december.spring.studywithme.entity.UserType;
import december.spring.studywithme.exception.UserException;
import december.spring.studywithme.jwt.JwtUtil;
import december.spring.studywithme.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	@Mock
	UserRepository userRepository;
	
	@Mock
	PasswordEncoder passwordEncoder;
	
	@Mock
	JwtUtil jwtUtil;
	
	@InjectMocks
	UserService userService;
	
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
	
	public UserRequestDTO createUserRequestDTO() {
		UserRequestDTO userRequestDTO;
		userRequestDTO = new UserRequestDTO();
		userRequestDTO.setUserId("testId");
		userRequestDTO.setPassword("testPassword!1212");
		userRequestDTO.setEmail("testtest@gmail.com");
		userRequestDTO.setIntroduce("테스트입니다.");
		
		return userRequestDTO;
	}
	
	public UserProfileRequestDTO createUserProfileRequestDTO() {
		UserProfileRequestDTO userProfileRequestDTO;
		userProfileRequestDTO = new UserProfileRequestDTO();
		userProfileRequestDTO.setUserId("testId");
		userProfileRequestDTO.setName("testName");
		userProfileRequestDTO.setIntroduce("update update");
		userProfileRequestDTO.setCurrentPassword("testPassword!1212");
		
		return userProfileRequestDTO;
	}
	
	
	public PasswordRequestDTO createPasswordRequestDTO() {
		PasswordRequestDTO passwordRequestDTO;
		passwordRequestDTO = new PasswordRequestDTO();
		passwordRequestDTO.setPassword("testPassword!1212");
		
		return passwordRequestDTO;
	}
	
	public EditPasswordRequestDTO createEditPasswordRequestDTO() {
		EditPasswordRequestDTO editPasswordRequestDTO;
		editPasswordRequestDTO = new EditPasswordRequestDTO();
		editPasswordRequestDTO.setCurrentPassword("encodedPassword");
		editPasswordRequestDTO.setNewPassword("newPassword!1212");
		
		return editPasswordRequestDTO;
	}
	
	@Test
	@DisplayName("회원가입 성공")
	void 회원가입성공() {
		//given
		UserRequestDTO userRequestDTO = createUserRequestDTO();
		
		when(userRepository.findByUserId(anyString())).thenReturn(Optional.empty());
		when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
		when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

		User user = User.builder()
			.userId(userRequestDTO.getUserId())
			.password("encodedPassword")
			.name(userRequestDTO.getName())
			.email(userRequestDTO.getEmail())
			.userType(UserType.UNVERIFIED)
			.introduce(userRequestDTO.getIntroduce())
			.build();

		when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
		
		//when
		UserResponseDTO responseDTO = userService.createUser(userRequestDTO);
		
		//then
		assertThat(responseDTO).isNotNull();
		assertThat(responseDTO.getUserId()).isEqualTo(userRequestDTO.getUserId());
		assertThat(responseDTO.getName()).isEqualTo(userRequestDTO.getName());
		assertThat(responseDTO.getEmail()).isEqualTo(userRequestDTO.getEmail());
		assertThat(responseDTO.getIntroduce()).isEqualTo(userRequestDTO.getIntroduce());
	}
	
	@Test
	@DisplayName("회원가입 실패 - 이미 존재하는 아이디")        
	void 회원가입실패_존재하는아이디() {
	    //given
		UserRequestDTO userRequestDTO = createUserRequestDTO();
	    when(userRepository.findByUserId(anyString())).thenReturn(Optional.of(User.builder().build()));
		
	    //when - then
		assertThatThrownBy(() -> userService.createUser(userRequestDTO))
			.isInstanceOf(UserException.class)
			.hasMessageContaining("중복된 id 입니다.");
	}
	
	@Test
	@DisplayName("회원가입 실패 - 이미 존재하는 이메일")
	void 회원가입실패_존재하는이메일() {
	    //given
		UserRequestDTO userRequestDTO = createUserRequestDTO();
		when(userRepository.findByUserId(anyString())).thenReturn(Optional.empty());
		when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(User.builder().build()));
		
		//when - then
	    assertThatThrownBy(() -> userService.createUser(userRequestDTO))
			.isInstanceOf(UserException.class)
			.hasMessageContaining("중복된 Email 입니다.");
	}
	
	@Test
	@DisplayName("회원 활성화 성공")
	void 회원활성화성공() {
	    //given
		User user = User.builder()
			.userId("testId")
			.password("encodedPassword")
			.name("testName")
			.email("1234@gmail.com")
			.userType(UserType.UNVERIFIED)
			.introduce("testIntroduce")
			.build();
		
		when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
		
	    //when
		userService.updateUserActive(user);
	    
	    //then
		assertThat(user.getUserType()).isEqualTo(UserType.ACTIVE);
	}
	
	@Test
	@DisplayName("회원 탈퇴 성공")
	void 회원탈퇴성공() {
	    //given
		User user = createUser();
		PasswordRequestDTO requestDTO = createPasswordRequestDTO();
		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
		
	    //when
		userService.withdrawUser(requestDTO, user);
		
	    //then
		assertThat(user.getUserType()).isEqualTo(UserType.DEACTIVATED);
	}
	
	@Test
	@DisplayName("회원 탈퇴 실패 - 탈퇴한 회원")
	void 회원탈퇴실패_탈퇴한회원() {
		//given
		PasswordRequestDTO requestDTO = createPasswordRequestDTO();
		User user = User.builder()
			.userType(UserType.DEACTIVATED)
			.build();
		
		//when - then
		assertThatThrownBy(() -> userService.withdrawUser(requestDTO, user))
			.isInstanceOf(UserException.class)
			.hasMessageContaining("이미 탈퇴한 회원입니다.");
		
	}
	
	@Test
	@DisplayName("회원 탈퇴 실패 - 비밀번호 불일치")
	void 회원탈퇴실패_비밀번호불일치() {
	    //given
	    PasswordRequestDTO requestDTO = createPasswordRequestDTO();
		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
		
	    //when - then
		assertThatThrownBy(() -> userService.withdrawUser(requestDTO, createUser()))
			.isInstanceOf(UserException.class)
			.hasMessageContaining("비밀번호가 일치하지 않습니다.");
	}
	
	@Test
	@DisplayName("로그아웃 성공")
	void 로그아웃성공() {
	    //given
	    User user = createUser();
		String accessToken = "accessToken";
		String refreshToken = "refreshToken";
		when(userRepository.findByUserId(anyString())).thenReturn(Optional.of(User.builder().build()));
		
	    //when
	    userService.logout(user, accessToken, refreshToken);
		
	    //then
		verify(jwtUtil,times(1)).invalidateToken(accessToken);
		verify(jwtUtil,times(1)).invalidateToken(refreshToken);
		assertThat(user.getRefreshToken()).isNull();
	}
	
	@Test
	@DisplayName("로그아웃 실패 - 로그인 되어 있지 않은 유저")
	void 로그아웃실패_로그인되어있지않은유저() {
		//given
		User user = null;
		String accessToken = "accessToken";
		String refreshToken = "refreshToken";
		
		//when - then
		assertThatThrownBy(() -> userService.logout(user, accessToken, refreshToken))
			.isInstanceOf(UserException.class)
			.hasMessageContaining("로그인되어 있는 유저가 아닙니다.");
	}
	
	@Test
	@DisplayName("로그아웃 실패 - 탈퇴한 유저")
	void 로그아웃실패_탈퇴한유저() {
	    //given
	    User user = User.builder()
			.userType(UserType.DEACTIVATED)
			.build();
		
		//when - then
		assertThatThrownBy(() -> userService.logout(user, "accessToken", "refreshToken"))
			.isInstanceOf(UserException.class)
			.hasMessageContaining("이미 탈퇴한 회원입니다.");
	}
	
	@Test
	@DisplayName("로그인한 회원 조회 성공")
	void 로그인한회원조회성공() {
	    //given
	    User user = createUser();
		
	    //when
		UserProfileResponseDTO userProfileResponseDTO = userService.inquiryUser(user);
		
		//then
		assertThat(userProfileResponseDTO).isNotNull();
		assertThat(userProfileResponseDTO.getUserId()).isEqualTo(user.getUserId());
		assertThat(userProfileResponseDTO.getEmail()).isEqualTo(user.getEmail());
		assertThat(userProfileResponseDTO.getIntroduce()).isEqualTo(user.getIntroduce());
	}
	
	@Test
	@DisplayName("회원 조회 성공")
	void 회원조회성공() {
		//given
		User user = createUser();
		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		
		//when
		UserResponseDTO responseDTO = userService.inquiryUserById(1L);
		
		//then
		assertThat(responseDTO).isNotNull();
		assertThat(responseDTO.getUserId()).isEqualTo(user.getUserId());
		assertThat(responseDTO.getName()).isEqualTo(user.getName());
		assertThat(responseDTO.getEmail()).isEqualTo(user.getEmail());
		assertThat(responseDTO.getIntroduce()).isEqualTo(user.getIntroduce());
	}
	
	@Test
	@DisplayName("회원 조회 실패 - 존재하지 않는 회원")
	void 회원조회실패_존재하지않는회원() {
	    //given
	    when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
		
	    //when - then
		assertThatThrownBy(() -> userService.inquiryUserById(1L))
			.isInstanceOf(UserException.class)
			.hasMessageContaining("해당 유저를 찾을 수 없습니다.");
	}
	
	@Test
	@DisplayName("회원 프로필 수정 성공")
	void 회원프로필수정성공() {
	    //given
	    User user = createUser();
		UserProfileRequestDTO userProfileRequestDTO = createUserProfileRequestDTO();
		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
		
	    //when
		UserResponseDTO userResponseDTO = userService.editProfile(userProfileRequestDTO, user);
	    
	    //then
		assertThat(userResponseDTO).isNotNull();
		assertThat(userResponseDTO.getName()).isEqualTo(userProfileRequestDTO.getName());
		assertThat(userResponseDTO.getIntroduce()).isEqualTo(userProfileRequestDTO.getIntroduce());
	}
	
	@Test
	@DisplayName("회원 프로필 수정 실패 - 비밀번호 불일치")
	void 회원프로필수정실패_비밀번호불일치() {
	    //given
		User user = createUser();
		UserProfileRequestDTO userProfileRequestDTO = createUserProfileRequestDTO();
	    when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
		
	    //when - then
		assertThatThrownBy(() -> userService.editProfile(userProfileRequestDTO, user))
			.isInstanceOf(UserException.class)
			.hasMessageContaining("비밀번호가 일치하지 않습니다.");
	}
	
	@Test
	@DisplayName("비밀번호 변경 성공")
	void 비밀번호변경성공() {
	    //given
	    User user = createUser();
		EditPasswordRequestDTO editPasswordRequestDTO = createEditPasswordRequestDTO();
		when(passwordEncoder.matches(editPasswordRequestDTO.getCurrentPassword(), user.getPassword())).thenReturn(true);
		when(passwordEncoder.matches(editPasswordRequestDTO.getNewPassword(), user.getPassword())).thenReturn(false);
		
		when(passwordEncoder.encode(editPasswordRequestDTO.getNewPassword())).thenReturn("newPassword!1212");
		
	    //when
		UserResponseDTO userResponseDTO = userService.editPassword(editPasswordRequestDTO, user);
	    
	    //then
		assertThat(userResponseDTO).isNotNull();
		assertThat(user.getUserId()).isEqualTo(userResponseDTO.getUserId());
		assertThat(user.getPassword()).isEqualTo("newPassword!1212");
	}
	
	@Test
	@DisplayName("비밀번호 변경 실패 - 비밀번호 불일치")
	void 비밀번호변경실패_비밀번호불일치() {
	    //given
		User user = createUser();
		EditPasswordRequestDTO editPasswordRequestDTO = createEditPasswordRequestDTO();
		when(passwordEncoder.matches(editPasswordRequestDTO.getCurrentPassword(), user.getPassword())).thenReturn(false);
		
		//when - then
		assertThatThrownBy(() -> userService.editPassword(editPasswordRequestDTO, user))
			.isInstanceOf(UserException.class)
			.hasMessageContaining("현재 비밀번호와 일치하지 않습니다.");
	}
	
	@Test
	@DisplayName("비밀번호 변경 실패 - 새로운 비밀번호와 기존 비밀번호 동일")
	void 비밀번호변경실패_새로운비밀번호와기존비밀번호동일() {
	    //given
		User user = createUser();
		EditPasswordRequestDTO editPasswordRequestDTO = createEditPasswordRequestDTO();
		when(passwordEncoder.matches(editPasswordRequestDTO.getCurrentPassword(), user.getPassword())).thenReturn(true);
		when(passwordEncoder.matches(editPasswordRequestDTO.getNewPassword(), user.getPassword())).thenReturn(true);
		
	    //when - then
		assertThatThrownBy(() -> userService.editPassword(editPasswordRequestDTO, user))
			.isInstanceOf(UserException.class)
			.hasMessageContaining("새로운 비밀번호와 기존 비밀번호가 동일합니다.");
	}
	
	
}
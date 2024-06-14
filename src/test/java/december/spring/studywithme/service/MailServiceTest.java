package december.spring.studywithme.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.NoSuchAlgorithmException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import december.spring.studywithme.exception.EmailException;
import december.spring.studywithme.repository.CertificationNumberRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {
	@Mock
	JavaMailSender mailSender;
	
	@Mock
	CertificationNumberRepository certificationNumberRepository;
	
	@InjectMocks
	MailService mailService;
	
	private MimeMessage mimeMessage;
	
	@BeforeEach
	public void setup() {
		mimeMessage = mock(MimeMessage.class);
	}
	
	@Test
	@DisplayName("이메일 인증 코드 발송 성공")
	void 이메일인증코드발송() throws MessagingException, NoSuchAlgorithmException {
	    //given
		String email = "36-96@naver.com";
		String certificationNumber = "123456";
		
		MailService spyMailService = spy(mailService);
		
		doNothing().when(certificationNumberRepository).saveCertificationNumber(anyString(), anyString());
		when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
		doNothing().when(mailSender).send(any(MimeMessage.class));
		when(spyMailService.createCertificationNumber()).thenReturn(certificationNumber);

		// When
		String result = spyMailService.sendEmailForCertification(email);

		// Then
		assertEquals(email, result);
		verify(certificationNumberRepository, times(1)).saveCertificationNumber(anyString(), anyString());
		verify(mailSender, times(1)).send(any(MimeMessage.class));
	}
	
	@Test
	@DisplayName("메일 발송 성공")
	void 메일발송성공() throws MessagingException {
	    //given
	    String email = "test@example.com";
	    String content = "test";
		
		when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
		
	    //when
	    mailService.sendMail(email, content);
		
	    //then
		verify(mailSender, times(1)).createMimeMessage();
		verify(mailSender, times(1)).send(any(MimeMessage.class));
	}
	
	@Test
	@DisplayName("이메일 인증 확인 성공")
	void 이메일인증확인성공() {
	    //given
		String email = "36-96@naver.com";
		String certificationNumber = "123456";
		
		when(certificationNumberRepository.getCertificationNumber(email)).thenReturn(certificationNumber);
		
	    //when
		mailService.verifyEmail(email, certificationNumber);
		
	    //then
		verify(certificationNumberRepository, times(1)).getCertificationNumber(email);
		verify(certificationNumberRepository, times(1)).removeCertificationNumber(email);
	}
	
	@Test
	@DisplayName("이메일 인증 확인 실패 - 인증번호 불일치")
	void 이메일인증확인실패_인증번호불일치() {
	    //given
		String email = "36-96@naver.com";
		String certificationNumber = "123456";
		
		when(certificationNumberRepository.getCertificationNumber(email)).thenReturn("654321");
		
	    //when - then
		Assertions.assertThatThrownBy(() -> mailService.verifyEmail(email, certificationNumber))
			.isInstanceOf(EmailException.class)
			.hasMessage("인증번호가 일치하지 않습니다.");
	}
}
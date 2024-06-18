package december.spring.studywithme.integration.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import december.spring.studywithme.repository.CertificationNumberRepository;
import december.spring.studywithme.service.MailService;
import jakarta.mail.MessagingException;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class MailServiceTest {
	@Autowired
	MailService mailService;
	
	@Autowired
	private CertificationNumberRepository certificationNumberRepository;
	
	@Test
	@Order(1)
	@DisplayName("이메일 인증 코드 발송 테스트")
	void 이메일인증코드발송() throws MessagingException, NoSuchAlgorithmException {
	    //given
	    String email = "36-96@naver.com";
		
	    //when
		String result = mailService.sendEmailForCertification(email);
	    
	    //then
		assertThat(result).isEqualTo(email);
	}
	
	@Test
	@Order(2)
	@DisplayName("이메일 인증 확인 테스트")
	void 이메일인증확인() {
	    //given
	    String email = "36-96@naver.com";
		String certificationNumber = certificationNumberRepository.getCertificationNumber(email);
		
	    //when
		mailService.verifyEmail(email, certificationNumber);
		
		//then
		assertThat(certificationNumberRepository.getCertificationNumber(email)).isNull();
	}
}
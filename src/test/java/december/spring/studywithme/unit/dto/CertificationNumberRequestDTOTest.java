package december.spring.studywithme.unit.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import december.spring.studywithme.dto.CertificationNumberRequestDTO;

class CertificationNumberRequestDTOTest {
	@Test
	@DisplayName("CertificationNumberRequestDTO 생성 테스트")
	void DTO생성() {
	    //given
		CertificationNumberRequestDTO certificationNumberRequestDTO = new CertificationNumberRequestDTO();
		String code = "123456";
		
		//when
		certificationNumberRequestDTO.setCode(code);
	    
	    //then
		assertThat(certificationNumberRequestDTO.getCode()).isEqualTo(code);
	}
}
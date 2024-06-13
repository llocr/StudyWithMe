package december.spring.studywithme.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CertificationNumberRequestDTO {
	@NotBlank
	private String code;
}

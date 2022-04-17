package posmy.interview.boot.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberDto {
	private Long id;
	
    @NotBlank(message = "Fullname is mandatory")
	private String fullName;
    
	private String status;
}

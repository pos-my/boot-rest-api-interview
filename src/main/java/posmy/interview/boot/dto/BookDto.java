package posmy.interview.boot.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookDto {
	private Long id;
	
    @NotBlank(message = "Title is mandatory")
	private String title;    
    
    @NotBlank(message = "Description is mandatory")
	private String description;   
    
	private String borrowerName;	
	private String status;   
	private String remarks;
}

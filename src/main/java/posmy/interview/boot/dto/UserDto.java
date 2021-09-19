package posmy.interview.boot.dto;

import lombok.*;
import posmy.interview.boot.model.User;


@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    
	private Long userid;
	
	private String username;
	
	private String password;

	private String role;
    
	private String accStatus;
    
//	private Set<BookDto> book;
	
	public UserDto(User user) {
		this.userid = user.getUserid();
		this.password = user.getPassword();
		this.role = user.getRole().toString();
		this.accStatus = user.getAccStatus().toString();
		this.username = user.getUsername();		
	}

}
package posmy.interview.boot.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {
	
	private String username;
	
	private String password;
	
	private String firstname;
	
	private String lastname;
	
}

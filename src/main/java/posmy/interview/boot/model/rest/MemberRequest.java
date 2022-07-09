package posmy.interview.boot.model.rest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberRequest {

	private String username;
	
	private int age;
	
	private String firstName;
	
	private String lastName;
	
	private String password;
}

package posmy.interview.boot.object;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserObject implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String username;
	private String password;
	private String fullName;
	private Long role;
}

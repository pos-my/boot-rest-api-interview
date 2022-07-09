package posmy.interview.boot.constant;

public enum Roles {
	
	LIBRARIAN("ROLE_LIBRARIAN"),
	MEMBER("ROLE_MEMBER");
	
	private final String grantedAuthoritiesRole;
	
	
	Roles(String grantedAuthoritiesRole) {
		
		this.grantedAuthoritiesRole = grantedAuthoritiesRole;
	}
	
	public String getGrantedAuthoritiesRole() {
		return grantedAuthoritiesRole;
	}
}

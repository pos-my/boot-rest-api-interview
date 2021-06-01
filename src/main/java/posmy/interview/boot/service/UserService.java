package posmy.interview.boot.service;

import posmy.interview.boot.request.AccountRequest;
import posmy.interview.boot.request.UserRequest;
import posmy.interview.boot.response.BaseServiceResponse;

public interface UserService {
	
	BaseServiceResponse getAllUsers();
	
	BaseServiceResponse getUserById( long id );
	
	BaseServiceResponse getUserByUsername(String usetname );

	BaseServiceResponse addUser(UserRequest request);
	
	BaseServiceResponse updateUser( long id, UserRequest request );

	BaseServiceResponse deleteUser(long id);
	
	BaseServiceResponse deleteUserByUsername( String username );
	
	BaseServiceResponse addAccount(AccountRequest request);
	

}

package posmy.interview.boot.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import posmy.interview.boot.entity.Role;
import posmy.interview.boot.entity.User;
import posmy.interview.boot.enums.UserRole;
import posmy.interview.boot.enums.UserState;
import posmy.interview.boot.repository.RoleRepository;
import posmy.interview.boot.repository.UserRepository;
import posmy.interview.boot.request.AccountRequest;
import posmy.interview.boot.request.UserRequest;
import posmy.interview.boot.response.BaseServiceResponse;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
    private RoleRepository roleRepository;
	
	@Override
    public BaseServiceResponse getAllUsers() {
		List<User> result = userRepository.findAll();
		boolean success = (result  != null && !result.isEmpty() );
		String message = "User list found";
		if(result.isEmpty()) {
			message = "User list is empty";
		}
		return BaseServiceResponse.builder().success( success ).message( message ).result( result ).build();
    }
	
	@Override
    public BaseServiceResponse getUserById( long id ) {
		Optional<User> findUser = userRepository.findById( id );
		User result = null;
		boolean success = false;
		String message = "User does not exist";
		if( findUser.isPresent() ) {
			result = findUser.get();
			success = true;
			message = "User " + result.getUsername() + " found";
		}
		return BaseServiceResponse.builder().success( success ).message( message ).result( result ).build();
    }
	
	@Override
    public BaseServiceResponse getUserByUsername( String username ) {
		Optional<User> findUser = userRepository.findByUsername(username);
		User result = null;
		boolean success = false;
		String message = "User does not exist";
		if( findUser.isPresent() ) {
			result = findUser.get();
			success = true;
			message = "User " + result.getUsername() + " found";
		}
		return BaseServiceResponse.builder().success( success ).message( message ).result( result ).build();
    }
	
	
		
	@Override
    public BaseServiceResponse addUser( UserRequest request ) {
		User result = null;
		Optional<User> findUser = userRepository.findByUsername( request.getUsername() );
		boolean success = false;
		String message = "User " + request.getUsername() + " is already existed";
		if( !findUser.isPresent() ) {
			result = new User();
			
			if( request.getUsername() != null ) 
				result.setUsername( request.getUsername() );
			
			if( request.getPassword() != null ) 
				result.setPassword( request.getPassword() );
			
			if( request.getFirstname() != null ) 
				result.setFirstname( request.getFirstname() );
			
			if( request.getLastname() != null ) 
				result.setLastname( request.getLastname() );		
			
			if( request.getStatus() != null ) 
				result.setStatus( request.getStatus() );
			
			result = userRepository.save( result );
			
			List<UserRole> roles = request.getRoles();
			if( roles != null  && roles.size() > 0 ) {
				for(UserRole role: roles) {
					Optional<Role> findRole = roleRepository.findByRole( role );
					if( findRole.isPresent() ) {
						roleRepository.addUserRole( result.getId().toString() , findRole.get().getId().toString() );
					}
				}
			}
			success = true;
			message = "User added";
			
		}
		return BaseServiceResponse.builder().success( success ).message( message ).result( result ).build();
    }
	
	@Override
    public BaseServiceResponse updateUser( long id, UserRequest request ) {
		User result = null;
		Optional<User> updateUser = userRepository.findById( id );
		boolean success = false;
		String message = "User " + request.getUsername() + " does not exist";
		if( updateUser != null ) {
			Optional<User> findUser = null;
			if( request.getUsername() != null )
				findUser = userRepository.findByUsername( request.getUsername() );
			
			if( findUser == null || !findUser.isPresent() ) {
				result = updateUser.get();
				result.setId( id );
				
				if( request.getUsername() != null ) {
					result.setUsername( request.getUsername() );
				} 
				
				if( request.getPassword() != null ) {
					result.setPassword( request.getPassword() );
				} 
				
				if( request.getFirstname() != null ) {
					result.setFirstname( request.getFirstname() );
				} 
				
				if( request.getFirstname() != null ) {
					result.setLastname( request.getLastname() );
				} 

				if( request.getStatus() != null ) {
					result.setStatus( request.getStatus() );
				}
					
				userRepository.updateUser( String.valueOf( result.getId() ), result.getUsername(), 
						result.getPassword(), result.getFirstname(), 
						result.getLastname(), result.getStatus().name() );
				
				List<UserRole> roles = request.getRoles();
				if( roles != null  && roles.size() > 0 ) {
					roleRepository.deleteByUserId( id );
					for(UserRole role: roles) {
						Optional<Role> findRole = roleRepository.findByRole( role );
						if( findRole.isPresent() ) {
							roleRepository.addUserRole( String.valueOf( id ) , findRole.get().getId().toString() );
						}
					}
				}
				success = true;
				message = "User updated";
			} else {
				message = "User title '" + request.getUsername() + "' is already existed";
				result = null;
			}
		} else {
			result = null;
		}
		return BaseServiceResponse.builder().success( success ).message( message ).result( result ).build();
    }
	
	@Override
    public BaseServiceResponse deleteUser( long id ) {
		User result = null;
		Optional<User> findUser = userRepository.findById( id );
		boolean success = false;
		String message = "User does not exist";
		if( findUser.isPresent() ) {
			result = findUser.get();
			roleRepository.deleteByUserId( id );
			userRepository.deleteById( id );
			success = true;
			message = "User deleted";
		}
		return BaseServiceResponse.builder().success( success ).message( message ).result( result ).build();
    }
	
	@Override
    public BaseServiceResponse addAccount( AccountRequest request ) {
		User result = null;
		Optional<User> findUser = userRepository.findByUsername( request.getUsername() );
		boolean success = false;
		String message = "Username is already used";
		if( !findUser.isPresent() ) {
			result = new User();
			
			if( request.getUsername() != null ) 
				result.setUsername( request.getUsername() );
			
			if( request.getPassword() != null ) 
				result.setPassword( request.getPassword() );
			
			if( request.getFirstname() != null ) 
				result.setFirstname( request.getFirstname() );
			
			if( request.getLastname() != null ) 
				result.setLastname( request.getLastname() );		
			
			result.setStatus( UserState.ACTIVE );
			result.setRoles( new ArrayList<Role>( Arrays.asList( new Role[]{ new Role( UserRole.MEMBER ) } ) ));
			
			result = userRepository.save( result );
			
			success = true;
			message = "Account created";
		}
		return BaseServiceResponse.builder().success( success ).message( message ).result( result ).build();
    }

	@Override
	public BaseServiceResponse deleteUserByUsername(String username) {
		User result = null;
		Optional<User> findUser = userRepository.findByUsername(username);
		boolean success = false;
		String message = "User does not exist";
		if( findUser.isPresent() ) {
			result = findUser.get();
			roleRepository.deleteByUserId( result.getId() );
			userRepository.deleteById( result.getId() );
			success = true;
			message = "User deleted";
		}
		return BaseServiceResponse.builder().success( success ).message( message ).result( result ).build();
	}

}

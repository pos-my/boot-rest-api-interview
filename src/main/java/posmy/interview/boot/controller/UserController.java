package posmy.interview.boot.controller;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import posmy.interview.boot.dto.UserDto;
import posmy.interview.boot.model.User;
import posmy.interview.boot.service.UserService;

@RestController  
public class UserController{  

	@Autowired  
	UserService userService;  
	
	@GetMapping("/lib/user")  
	public ResponseEntity<?> getAllUser()   
	{  
			List<User> user = userService.getAllUser();
			List<UserDto> userDtos = user.stream().map(x->new UserDto(x)).collect(Collectors.toList());
			return ResponseEntity.ok(userDtos);
	}  
	
	@PostMapping("/lib/user")  
	public ResponseEntity<?> createUser(@RequestBody UserDto userDto)   
	{  

			User user = userService.addUser(new User(userDto));
			UserDto dto = new UserDto(user);
			URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(dto.getUserid())
                    .toUri();
			return ResponseEntity.created(location).build();
			//return new ResponseEntity<>(dto, HttpStatus.CREATED);  
	}
	
	@PutMapping("/lib/user")  
	public ResponseEntity<?> updateUser(@RequestBody UserDto userDto)   
	{  
		try {
			User userDtoCon = new User(userDto);
			User user = userService.updateUser(userDtoCon);
			UserDto dto = new UserDto(user);

			return ResponseEntity.ok(dto);  
		}catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@DeleteMapping("/lib/user/{userid}")  
	public ResponseEntity<?> deleteUser(Principal principal, @PathVariable("userid") Long userid)   
	{  
			User user=userService.deleteUser(userid, principal.getName());
			return new ResponseEntity<>(HttpStatus.OK);
	}  
	
	@DeleteMapping("/mem/user/del")  
	public ResponseEntity<?> deleteUser(Principal principal)   
	{  
			User user=userService.deleteUser(principal.getName());
			return new ResponseEntity<>(HttpStatus.OK);
	}  

}

package posmy.interview.boot.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import posmy.interview.boot.entity.User;
import posmy.interview.boot.object.UserObject;
import posmy.interview.boot.security.UserAuthenticationService;
import posmy.interview.boot.service.IUserService;

@Slf4j
@RestController
@RequestMapping("user")
public class UserController {
	@Autowired
	IUserService userService;
	
	  @NonNull
	  UserAuthenticationService authentication;
	
	@PostMapping("create")
	public ResponseEntity<User> create(@AuthenticationPrincipal final User user, @RequestBody UserObject userObject){
		ResponseEntity<User> rse = new ResponseEntity<User>(HttpStatus.OK);
		try {
			Optional<User> userEntity = userService.create(userObject);
			if(!userEntity.isPresent()) {
				rse = new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
			}else {
				rse = new ResponseEntity<User>(HttpStatus.OK);
			}
		}catch(Exception e) {
			log.error("CreateUser() -> Error: {}", e.toString());
			rse = new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return rse;
	}
	
	@PutMapping("update/{userId}")
	public ResponseEntity<User> update(@AuthenticationPrincipal final User user, @PathVariable("userId") Long userId, @RequestBody UserObject userObject){
		ResponseEntity<User> rse = new ResponseEntity<User>(HttpStatus.OK);
		try {
			Optional<User> userEntity = userService.update(userObject, userId);
			if(!userEntity.isPresent()) {
				rse = new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
			}else {
				rse = new ResponseEntity<User>(HttpStatus.OK);
			}
		}catch(Exception e) {
			log.error("Update User -> Error: {}", e.toString());
			rse = new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return rse;
	}
	
	@GetMapping("/all")
	public ResponseEntity<Optional<List<User>>>  getUserListAll() {
		ResponseEntity<Optional<List<User>>> rse = new ResponseEntity<Optional<List<User>>> (HttpStatus.OK);
		try {
			rse = new ResponseEntity<Optional<List<User>>>(userService.findAll(), HttpStatus.OK);			
		}catch(Exception e){
			log.error("List All User -> Error: {}", e.toString());
			rse = new ResponseEntity<Optional<List<User>>> (HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return rse;
	}
	
	@GetMapping("get/{userId}")
	public ResponseEntity<User> getUserByUserId(@AuthenticationPrincipal User user, @PathVariable("userId") Long userId) {
		ResponseEntity<User> rse = new ResponseEntity<User>(HttpStatus.OK);
		try {
			Optional<User> userEntity = userService.findByUserId(userId);
			if(!userEntity.isPresent()) {
				rse = new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
			}else {
				rse = new ResponseEntity<User>(userEntity.get(), HttpStatus.OK);
			}
		}catch(Exception e){
			log.error("User by Id -> Error: {}", e.toString());
			rse = new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return rse;
	}
	
	@DeleteMapping("delete/{userId}")
	public ResponseEntity<Boolean> delete(@AuthenticationPrincipal User user, @PathVariable("userId") Long userId) {
		ResponseEntity<Boolean> rse = new ResponseEntity<Boolean>(HttpStatus.OK);
		try {
			Optional<Boolean> userEntity = Optional.of(false);
			if(user.getRole().getName().equals("LIBRARIAN")) {
				userEntity = userService.delete(userId);
			}
			else if(user.getRole().getName().equals("MEMBER") && user.getUserId() == userId) {
				userEntity = userService.delete(userId);
			}else {
				rse = new ResponseEntity<Boolean>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			if(!userEntity.isPresent()) {
				rse = new ResponseEntity<Boolean>(HttpStatus.INTERNAL_SERVER_ERROR);
			}else {
				rse = new ResponseEntity<Boolean>(userEntity.get(), HttpStatus.OK);
			}
		}catch(Exception e){
			log.error("Delete User by Id -> Error: {}", e.toString());
			rse = new ResponseEntity<Boolean>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return rse;
		
	}	
}

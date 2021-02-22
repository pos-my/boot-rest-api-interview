package posmy.interview.boot.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import posmy.interview.boot.config.TokenProvider;
import posmy.interview.boot.dao.RoleDao;
import posmy.interview.boot.dao.UserDao;
import posmy.interview.boot.entity.User;
import posmy.interview.boot.model.UserInfo;
import posmy.interview.boot.model.requestModel.FirstTimeRegisterRequest;
import posmy.interview.boot.model.requestModel.LoginRequest;
import posmy.interview.boot.model.requestModel.RegisterRequest;
import posmy.interview.boot.model.requestModel.UpdateUserRequest;
import posmy.interview.boot.model.responseModel.JwtResponse;
import posmy.interview.boot.model.responseModel.MessageResponse;
import posmy.interview.boot.service.UserService;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider jwtTokenUtil;

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserDao dao;
    
    @Autowired
    private RoleDao roleDao;
    
    @Autowired
	PasswordEncoder encoder;
    
       
    
    @PostMapping("/signin")
	public ResponseEntity<?> authenticateUser( @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtTokenUtil.generateJwtToken(authentication);
		
		UserInfo userDetails = (UserInfo) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt, 
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 roles));
	}

    @PostMapping("/firstSignup")
	public ResponseEntity<?> firstTimeRegister(@Valid @RequestBody FirstTimeRegisterRequest signUpRequest) {
		
    	
    	List userLs = userService.findAll();    	
    	if (userLs != null && userLs.size()>0) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: This can only be used for First Time Registration!"));
		}


    	userService.saveFirstRegister(signUpRequest);

    	
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

    
	@PostMapping("/signup")
	@PreAuthorize("hasRole('LIBRARIAN')")
	public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest signUpRequest) {
		if (dao.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}


		userService.saveRegister(signUpRequest);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
	
	@GetMapping("/getAllUsers")
	@PreAuthorize("hasRole('LIBRARIAN')")
	public List<User> getAllUser() {
		return userService.findAll();
	}
	
	@GetMapping("/getUsers/{id}")
	@PreAuthorize("hasRole('LIBRARIAN')")
	public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long id)throws Exception {
		User user = dao.findById(id).orElseThrow(() -> new RuntimeException("Error: User is not found."));	
		
		return ResponseEntity.ok().body(user);
	}
	
	
	@PutMapping("/update/{id}")
	@PreAuthorize("hasRole('LIBRARIAN')")
	public ResponseEntity<?> updateUser(@PathVariable(value="id") Long id, @Valid @RequestBody UpdateUserRequest request) throws Exception{
		
		User user = dao.findById(id).orElseThrow(() -> new RuntimeException("Error: User is not found."));
		
		if(request.getUsername()!= null && request.getUsername().length()>0) {
			if (dao.existsByUsername(request.getUsername())) {
				return ResponseEntity
						.badRequest()
						.body(new MessageResponse("Username already exist."));
			}
			
			user.setUsername(request.getUsername());
		}
		
		if(request.getPassword()!= null && request.getPassword().length()>0) {
			user.setPassword( encoder.encode(request.getPassword()));
		}
		
	    
	    final User updatedUser = dao.save(user);
	    return ResponseEntity.ok(updatedUser);
		
	}
	
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasRole('LIBRARIAN')")
	public Map<String, Boolean> deleteUser(@PathVariable(value="id") Long id) throws Exception{
	    Map<String, Boolean> mapResponse = new HashMap<>();
		User user = dao.findById(id).orElseThrow(() -> new RuntimeException("Error: User is not found."));
		
		if(user.getBook() != null && user.getBook().size()>0) {
		    mapResponse.put("Unable to Delete due to user still has pending books", Boolean.TRUE);
		    return mapResponse;

		}
		
	    dao.delete(user);
	    mapResponse.put("deleted", Boolean.TRUE);
	    return mapResponse;
		
	}
	
	@GetMapping("/admin")
	@PreAuthorize("hasRole('LIBRARIAN')")
	public String adminAccess() {
		return "Admin Board.";
	}
	
    @PreAuthorize("hasRole('LIBRARIAN')")
    @RequestMapping(value="/adminping", method = RequestMethod.GET)
    public String adminPing(HttpServletRequest request){
    	
    	String token = request.getHeader("Authorization");
		
    	System.out.println(request);
		System.out.println(token);
		if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
			token=  token.substring(7, token.length());
		}
		
		String sessionUsername = jwtTokenUtil.getUserNameFromJwtToken(token);
        return "Only Admins Can Read This";
    }
    
    
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value="/userping", method = RequestMethod.GET)
    public String userPing(){
        return "Any User Can Read This";
    }

}

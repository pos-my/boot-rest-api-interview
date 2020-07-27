package posmy.interview.boot.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import posmy.interview.boot.dto.UserDto;
import posmy.interview.boot.service.UserService;

@Controller
@RequestMapping("users")
public class UserResource {

	@Autowired
	private UserService userService;
	
	@PostMapping("/new")
	public String createNewUser(@ModelAttribute("user") UserDto userDto) {
		userService.saveUser(userDto);
		return "redirect:/view-users";
	}

	@PostMapping("/update")
	public String updateUser(@ModelAttribute("user") UserDto userDto) {
		userService.updateUser(userDto);
		return "redirect:/view-users";
	}

	@PostMapping("/delete/{id}")
	public String deleteUser(@PathVariable Long id) {
		userService.deleteUserById(id);
		return "redirect:/view-users";
	}
	
	@PostMapping("/delete-all")
	public String deleteAllUsers() {
		userService.deleteAllUsers();
		return "redirect:/view-users";
	}

}

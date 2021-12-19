package posmy.interview.boot.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import posmy.interview.boot.Book;
import posmy.interview.boot.RoleType;
import posmy.interview.boot.User;
import posmy.interview.boot.service.SystemService;

@RestController
@RequestMapping("/system")
public class SystemController {

	public static final Logger log = LoggerFactory.getLogger(SystemController.class);

	@Autowired
	private SystemService systemService;

	// check user status
	@GetMapping("/check/user/status")
	public User user(@RequestParam String username) throws Exception {
		User user = systemService.findByUsername(username);
		if (Boolean.TRUE == user.isDeleted()) {
			throw new Exception("Your account has been deleted. Please contact Librarian");
		} else {
			return user;
		}
	}
	
	// for librarian see the member info
	// for user see their own info
	@GetMapping("/view/member")
	public User member(@RequestParam String username) {
		User user1 = systemService.findRoleUser(username);

		User user = new User();
		if (RoleType.LIBRARIAN.getValue() == user1.getRoleId()) {
			// find all member info except librarian
			user = systemService.findUserByRole(RoleType.MEMBER.getValue());
		} else {
			// find own info
			user = systemService.findByUsername(username);
			log.info("== User Info == ");
			log.info(user.toString());
		}

		return user;
	}

	// librarian add book
	@PostMapping("/add/{username}")
	public Book addBook(@PathVariable String username, @RequestBody Book reqBook) throws Exception {
		User user = systemService.findRoleUser(username);
		Book book = new Book();

		if (reqBook == null) {
			throw new Exception("Book details is mandatory cannot be null");
		}

		if (RoleType.MEMBER.getValue() == user.getRoleId()) {
			throw new Exception("This user unable to add book");
		} else {
			book = systemService.addBook(reqBook);
		}
		return book;
	}

	// librarian update book
	@PutMapping("/update/{username}")
	public String updateBook(@PathVariable String username, @RequestBody Book reqBook) throws Exception {
		User user = systemService.findRoleUser(username);

		if (reqBook == null) {
			throw new Exception("Book details is mandatory cannot be null");
		}

		if (RoleType.MEMBER.getValue() == user.getRoleId()) {
			throw new Exception("This user unable to update book details");
		} else {
			systemService.updateBook(reqBook);
		}
		return "Successfully update";
	}

	// librarian remove book
	@PutMapping("/remove/{username}")
	public String removeBook(@PathVariable String username, @RequestBody Book reqBook) throws Exception {
		User user = systemService.findRoleUser(username);
		Book book = new Book();

		if (reqBook == null) {
			throw new Exception("Book details is mandatory cannot be null");
		}

		if (RoleType.MEMBER.getValue() == user.getRoleId()) {
			throw new Exception("This user unable to update book details");
		} else {
			// check the book has available
			book = systemService.findBookStatus(reqBook);
			if (book != null) {
				systemService.removeBook(reqBook);
			} else {
				throw new Exception("Book has been view/borrow by others");
			}

		}
		return "Successful remove book";
	}

	// librarian add member
	@PostMapping("/add/member")
	public User addMember(@RequestBody User reqUser) throws Exception {
		User u = new User();

		if (reqUser == null) {
			throw new Exception("User details is mandatory cannot be null");
		} else {
			if (reqUser.getUsername() != null) {
				u = systemService.findRoleUser(reqUser.getUsername());
			} else {
				throw new Exception("Username is mandatory");
			}
		}

		if (RoleType.MEMBER.getValue() == u.getRoleId()) {
			throw new Exception("This user cannot add member");
		} else {
			reqUser.setRoleId(RoleType.MEMBER.getValue());
			u = systemService.addMember(reqUser);
		}
		return u;
	}

	// librarian update member
	@PutMapping("/update/member")
	public String updateMember(@RequestBody User reqUser) throws Exception {
		User u = new User();

		if (reqUser == null) {
			throw new Exception("User details is mandatory cannot be null");
		} else {
			if (reqUser.getUsername() != null) {
				u = systemService.findRoleUser(reqUser.getUsername());
			} else {
				throw new Exception("Username is mandatory");
			}
		}

		if (RoleType.MEMBER.getValue() == u.getRoleId()) {
			throw new Exception("This user cannot update member info");
		} else {
			reqUser.setRoleId(RoleType.MEMBER.getValue());
			// find is existing user
			u = systemService.findByUsername(reqUser.getUsername());
			if (u != null) {
				systemService.updateMember(reqUser);
			} else {
				throw new Exception("Member not found.");
			}
		}
		return "Successful update";
	}

	// librarian remove member
	@PutMapping("/remove/member")
	public User removeMember(@RequestBody User reqUser) throws Exception {
		User u = new User();

		if (reqUser == null) {
			throw new Exception("User details is mandatory cannot be null");
		} 

		if (RoleType.MEMBER.getValue() == reqUser.getRoleId()) {
			throw new Exception("This user cannot update member info");
		} else {
			reqUser.setRoleId(RoleType.MEMBER.getValue());
			// find is existing user
			u = systemService.findByUsername(reqUser.getUsername());
			if (u != null) {
				// check the user has borrowed the book
				List<Book> b = systemService.findBookUserId(reqUser.getId());
				if (b.size() > 0) {
					throw new Exception("Unable to remove this member because this member havent return the book.");
				} else {
					systemService.removeMember(reqUser);
				}
			} else {
				throw new Exception("Member not found.");
			}
		}
		return u;
	}

	// only for member view book
	@GetMapping("/view/book")
	public List<Book> book(@RequestParam String username) throws Exception {
		User user = systemService.findRoleUser(username);
		List<Book> book = new ArrayList<Book>();

		if (RoleType.LIBRARIAN.getValue() == user.getRoleId()) {
			throw new Exception("This user unable to view book");
		} else {
			book = systemService.findBookAvailable();
		}
		return book;
	}

	// member borrow book
	@PutMapping("/borrow/book/{username}")
	public String borrowBook(@PathVariable String username, @RequestBody List<Book> bookReq) throws Exception {
		User user = systemService.findRoleUser(username);

		if (bookReq.size() < 1) {
			throw new Exception("Please select the book need to borrow");
		}

		if (RoleType.LIBRARIAN.getValue() == user.getRoleId()) {
			throw new Exception("This user unable to borrow book");
		} else {
			systemService.borrowBook(bookReq, user.getId());
		}
		return "Successful borrow the book";
	}

	// member return book
	@PutMapping("/return/book/{username}")
	public String returnBook(@PathVariable String username, @RequestBody List<Book> bookReq) throws Exception {
		User user = systemService.findRoleUser(username);

		if (bookReq.size() < 1) {
			throw new Exception("Please select the book need to return");
		}

		if (RoleType.LIBRARIAN.getValue() == user.getRoleId()) {
			throw new Exception("This user unable to borrow book");
		} else {
			systemService.returnBook(bookReq);
		}
		return "Successfully Return Book";
	}

	// member delete own account
	@PutMapping("/delete/account")
	public String deleteOwn(@RequestBody User user) throws Exception {
		if (RoleType.LIBRARIAN.getValue() == user.getRoleId()) {
			throw new Exception("This user unable to borrow book");
		} else {
			// check book borrowed before delete own account
			List<Book> b = systemService.findBookUserId(user.getId());
			if (b.size() > 0) {
				throw new Exception("Please return the book before delete your account");
			} else {
				systemService.deleteAcc(user.getId());
			}
		}
		return "Successfully Delete Your Own Account";
	}

}

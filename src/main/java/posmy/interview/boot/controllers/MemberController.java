package posmy.interview.boot.controllers;


import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import posmy.interview.boot.controllers.dtos.UserReq;
import posmy.interview.boot.domains.Users;
import posmy.interview.boot.repositories.UserRepository;

import java.util.List;
import java.util.Optional;


@RestController
@Slf4j
@RequestMapping("/member")
@SecurityRequirement(name = "assessment-api")
public class MemberController {
    private UserRepository userRepo;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public MemberController(UserRepository userRepo, @Qualifier("passwordEncoder") PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/list")
    @PreAuthorize("hasAuthority('LIBRARIAN') or hasAuthority('MEMBER')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Members fetched successfully",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Users.class))}),
            @ApiResponse(responseCode = "404", description = "No Members found", content = @Content)})
    public List<Users> listUsers() {
        return userRepo.findAll();
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/remove/{userName}")
    @PreAuthorize("hasAuthority('LIBRARIAN') or hasAuthority('MEMBER')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Members removed successfully",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Users.class))}),
            @ApiResponse(responseCode = "404", description = "Member not found", content = @Content)})
    public long removeUser(@PathVariable("userName") String userName) {
        return userRepo.deleteByUserName(userName);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/add")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member added successfully",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "403", description = "Only LIBRARIAN could add member", content = @Content)})
    public String addUser(@RequestBody final UserReq user) {
        Optional<Users> u = userRepo.findByUserName(user.getUserName());
        if (u.isPresent())
            throw new RuntimeException("Member already exist.");
        else {
            userRepo.save(new Users(passwordEncoder, user.getUserName(), user.getRole(), user.getStatus()));
            return user.getUserName() + " added into system.";
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/update")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member updated successfully",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Users.class))}),
            @ApiResponse(responseCode = "403", description = "Only LIBRARIAN could update member", content = @Content),
            @ApiResponse(responseCode = "404", description = "Member not found", content = @Content)})
    public Users updateUser(@RequestBody UserReq userReq) {
        Users user = userRepo.findByUserName(userReq.getUserName()).orElseThrow(() -> new RuntimeException("Member not found"));
        if (null != user) {
            BeanUtils.copyProperties(userReq, user);
            userRepo.saveAndFlush(user);
        }
        return user;
    }
}

package posmy.interview.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.model.Member;
import posmy.interview.boot.repository.MemberRepository;

import java.util.List;

@RestController
@RequestMapping("/userManagement")
public class UserManagementController {
    private final MemberRepository memberRepository;

    @Autowired
    public UserManagementController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @PostMapping("/user")
    public Member saveUser(@RequestBody Member member){
        return memberRepository.save(member);
    }

    @GetMapping("/user/{id}")
    public Member getUser(@RequestParam("id") Long id){
        return memberRepository.getReferenceById(id);
    }

    // get all user, for performance, we need to do pagination
    @GetMapping("/user/all")
    public List<Member> getAllUser(){
        return memberRepository.findAll();
    }

    @PutMapping("/user")
    public Member updateUser(@RequestBody Member member){
        return memberRepository.save(member);
    }

    @DeleteMapping("/user")
    public void deleteUser(@RequestParam("id") Long id){
        memberRepository.deleteById(id);
    }
}


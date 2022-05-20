package posmy.interview.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.model.Member;
import posmy.interview.boot.request.GenericReq;
import posmy.interview.boot.service.MemberService;

import java.util.List;
import java.util.Optional;

/**
 * Pour member service
 */
@Controller
@RequestMapping(value = "/librarian/member")
public class LibrarianMemberController {

    @Autowired
    MemberService memberService;

    @GetMapping(value = "/list", produces = "application/json")
    public List<Member> getMembers() {
        return memberService.viewAll();
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public Optional<Member> getMember(@PathVariable Long id) {
        return memberService.get(id);
    }

    @PostMapping(value = "/add", produces = "application/json")
    public Member addMember(@RequestBody final GenericReq<Member> req) {
        return memberService.add(req.getBody());
    }

    @PutMapping(value = "/update", produces = "application/json")
    public Member updateMember(@RequestBody final GenericReq<Member> req) {
        return memberService.update(req.getBody());
    }

    @PutMapping(value = "/delete", produces = "application/json")
    public Optional<Member> deleteMember(@RequestBody final GenericReq<Long> req) {
        return memberService.delete(req.getBody());
    }

}

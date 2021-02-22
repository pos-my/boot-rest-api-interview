package posmy.interview.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.model.Member;
import posmy.interview.boot.service.MemberService;

import java.util.List;

@RestController
public class MemberController {

    @Autowired
    MemberService memberService;

    @GetMapping("/member")
    private List<Member> getAllMember()
    {
        return memberService.getAllMember();
    }

    @GetMapping("/member/{memberid}")
    private Member getMember(@PathVariable("memberid") int memberid)
    {
        return memberService.getMemberById(memberid);
    }
    @DeleteMapping("/member/{memberid}")
    private void deleteMember(@PathVariable("memberid") int memberid)
    {
        memberService.delete(memberid);
    }
    @PostMapping("/member")
    private void saveMember(@RequestBody Member member)
    {
        memberService.saveOrUpdate(member);

    }

    @PutMapping("/member")
    private Member update(@RequestBody Member member)
    {
        memberService.saveOrUpdate(member);
        return member;
    }
}

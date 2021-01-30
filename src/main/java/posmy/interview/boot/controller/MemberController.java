package posmy.interview.boot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import posmy.interview.boot.entity.MemberEntity;
import posmy.interview.boot.exception.LmsException;
import posmy.interview.boot.exception.NoDataFoundException;
import posmy.interview.boot.model.CreateMemberRequest;
import posmy.interview.boot.model.Member;
import posmy.interview.boot.model.UpdateMemberRequest;
import posmy.interview.boot.service.member.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping(value = "/{memberId}")
    public ResponseEntity<Member> getMember(@PathVariable long memberId) {
        try {
            return ResponseEntity.ok(memberService.getMember(memberId));
        } catch (NoDataFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Member Not Found", e);
        }
    }

    @PostMapping
    public ResponseEntity<MemberEntity> addMember(@RequestBody CreateMemberRequest createMemberRequest) {
        return ResponseEntity.ok(memberService.createMember(createMemberRequest));
    }

    @DeleteMapping(value = "/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable long memberId) {
        try {
            memberService.deleteMember(memberId);
        } catch (NoDataFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Member Not Found", e);
        }
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<MemberEntity> updateMemberDetails(@RequestBody UpdateMemberRequest updateMemberRequest) {
        try {
            return ResponseEntity.ok(memberService.updateMember(updateMemberRequest));
        } catch (NoDataFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}

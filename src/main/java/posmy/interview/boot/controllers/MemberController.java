package posmy.interview.boot.controllers;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.exceptions.CustomRestApiException;
import posmy.interview.boot.models.daos.Member;
import posmy.interview.boot.models.dtos.member.MemberDto;
import posmy.interview.boot.models.dtos.member.UserRole;
import posmy.interview.boot.services.MemberService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "members", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class MemberController {

    private Logger logger = LoggerFactory.getLogger(MemberController.class);

    private MemberService memberService;
    private ModelMapper modelMapper;

    @Autowired
    public MemberController(MemberService memberService, ModelMapper modelMapper) {
        this.memberService = memberService;
        this.modelMapper = modelMapper;
    }

    // Get member by ID
    @GetMapping(path = "/{memberId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMember(@RequestHeader(value = "Email", required = true) String email,
                                       @PathVariable("memberId") Long memberId) throws Exception {
        logger.info("Getting member with id: {}", memberId);
        if (email.isEmpty() || email.isBlank()) {
            logger.error("Missing header!");
            throw new CustomRestApiException("Missing credentials!", HttpStatus.UNAUTHORIZED);
        }
        UserRole role = memberService.getMemberUserRole(email);
        if (role.equals(UserRole.MEMBER)) {
            throw new CustomRestApiException("You are not allowed to access this request", HttpStatus.FORBIDDEN);
        }
        Member memberResponse = memberService.getMemberById(memberId);
        MemberDto response = modelMapper.map(memberResponse, MemberDto.class);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Get all members
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMembers(@RequestHeader(value = "Email", required = true) String email) throws Exception {
        logger.info("Getting all members.");
        if (email.isEmpty() || email.isBlank()) {
            logger.error("Missing header!");
            throw new CustomRestApiException("Missing credentials!", HttpStatus.UNAUTHORIZED);
        }
        UserRole role = memberService.getMemberUserRole(email);
        if (role.equals(UserRole.MEMBER)) {
            throw new CustomRestApiException("You are not allowed to access this request", HttpStatus.FORBIDDEN);
        }
        List<MemberDto> memberListResponse = new ArrayList<>();
        List<Member> memberList = memberService.getAllMembers();
        for (Member member : memberList) {
            MemberDto memberDto = modelMapper.map(member, MemberDto.class);
            memberListResponse.add(memberDto);
        }
        return ResponseEntity.status(HttpStatus.OK).body(memberListResponse);
    }

    // Add new member(s)
    @PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addMembers(@RequestHeader(value = "Email", required = true) String email,
                                        @Valid @RequestBody() List<MemberDto> request) throws Exception {
        logger.info("Adding new member(s).");
        if (email.isEmpty() || email.isBlank()) {
            logger.error("Missing header!");
            throw new CustomRestApiException("Missing credentials!", HttpStatus.UNAUTHORIZED);
        }
        UserRole role = memberService.getMemberUserRole(email);
        if (role.equals(UserRole.MEMBER)) {
            throw new CustomRestApiException("You are not allowed to access this request", HttpStatus.FORBIDDEN);
        }
        List<Member> memberListRequest = new ArrayList<>();
        for (MemberDto memberDtoRequest : request) {
            Member memberRequest = modelMapper.map(memberDtoRequest, Member.class);
            memberListRequest.add(memberRequest);
        }
        var response = memberService.addMembers(memberListRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Update member by ID
    @PutMapping(path = "/{memberId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateMember(@RequestHeader(value = "Email", required = true) String email,
                                          @PathVariable("memberId") Long memberId, @Valid @RequestBody() MemberDto request)
            throws Exception {
        logger.info("Updating member with id: {}.", memberId);
        if (email.isEmpty() || email.isBlank()) {
            logger.error("Missing header!");
            throw new CustomRestApiException("Missing credentials!", HttpStatus.UNAUTHORIZED);
        }
        UserRole role = memberService.getMemberUserRole(email);
        if (role.equals(UserRole.MEMBER)) {
            throw new CustomRestApiException("You are not allowed to access this request", HttpStatus.FORBIDDEN);
        }
        Member memberRequest = modelMapper.map(request, Member.class);
        var response = memberService.updateMemberById(memberId, memberRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Remove member by ID
    @DeleteMapping(path = "/remove/{memberId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteMember(@RequestHeader(value = "Email", required = true) String email,
                                          @PathVariable("memberId") Long memberId) throws Exception {
        logger.info("Removing member with id: {}.", memberId);
        if (email.isEmpty() || email.isBlank()) {
            logger.error("Missing header!");
            throw new CustomRestApiException("Missing credentials!", HttpStatus.UNAUTHORIZED);
        }
        UserRole role = memberService.getMemberUserRole(email);
        if (role.equals(UserRole.MEMBER)) {
            throw new CustomRestApiException("You are not allowed to access this request", HttpStatus.FORBIDDEN);
        }
        memberService.getMemberUserRole(email);
        var response = memberService.removeMemberById(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
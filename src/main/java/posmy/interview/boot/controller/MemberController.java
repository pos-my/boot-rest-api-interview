package posmy.interview.boot.controller;


import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import posmy.interview.boot.model.rest.GetMemberRequest;
import posmy.interview.boot.model.rest.GetMemberResponse;
import posmy.interview.boot.model.rest.MemberRequest;
import posmy.interview.boot.model.rest.MemberResponse;
import posmy.interview.boot.service.MemberService;

@Slf4j
@RestController
@RequestMapping({"/member"})
public class MemberController {

	private MemberService memberServiceImpl;
	
	public MemberController(MemberService memberServiceImpl) {
		this.memberServiceImpl = memberServiceImpl;
	}
	
	@RequestMapping(value="/viewAllMember", method = RequestMethod.GET)
	public ResponseEntity<Object> viewMember(){
		log.info("View Member Details");
		
		GetMemberResponse response = memberServiceImpl.getAllMember();
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value="/viewMemberByUsername", method = RequestMethod.GET)
	public ResponseEntity<Object> viewMemberByUsername(@Valid @RequestBody MemberRequest request) throws Exception{
		log.info("View Member Details by using username");
		
		MemberResponse response = memberServiceImpl.getMemberByUsername(request);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value="/addMember", method = RequestMethod.POST)
	public ResponseEntity<Object> addMember(@Valid @RequestBody MemberRequest request){
		log.info("Add Member Details");
		
		MemberResponse response =memberServiceImpl.addMember(request);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value="/updateMember", method = RequestMethod.POST)
	public ResponseEntity<Object> updateMember(@Valid @RequestBody MemberRequest request){
		log.info("Update Member Details");
		
		MemberResponse response =memberServiceImpl.updateMember(request);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value="/deleteMember", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteMember(@Valid @RequestBody GetMemberRequest request) throws Exception{
		log.info("Delete Member");
		
		String response = memberServiceImpl.deleteMember(request);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	

}

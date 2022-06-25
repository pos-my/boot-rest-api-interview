package posmy.interview.boot.api;

import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import posmy.interview.boot.constant.ErrorEnum;
import posmy.interview.boot.exception.WebserviceException;
import posmy.interview.boot.model.Member;
import posmy.interview.boot.service.MemberService;
import posmy.interview.boot.view.MemberView;

/**
 * @author Aboy
 * @version 0.01
 */
@CrossOrigin
@RestController
@RequestMapping("/member")
public class MemberController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MemberService memberService;

	/**
	 * Fetch a list of members
	 * @return a list of members
	 * @throws Exception 
	 */
	@RequestMapping(path="/allMembers", method = RequestMethod.GET, produces = "application/json")
	@ApiOperation(value = "Fetch all member")
	public ResponseEntity<?> member() throws Exception {
		List<Member> member = (List<Member>) memberService.findAllMembers();

		return new ResponseEntity<>(member, HttpStatus.OK);
	}

	/**
	 * Finds a member by <code>memberId</code>
	 * 
	 * @param memberId member's memberId
	 * 
	 * @return the {@link Member} object
	 * @throws Exception 
	 */
	@RequestMapping(path = "/getMemberByMemberId/{memberId}", 
			method = RequestMethod.GET)
	@ApiOperation(value = "Fetch a member")
	public ResponseEntity<?> member(@PathVariable String memberId) throws Exception {
		Member member = new Member();
		member = memberService.findByMemberId(memberId);
		
		return new ResponseEntity<>(member, HttpStatus.OK);
	}

	/**
	 * Add a member
	 * 
	 * @param memberView
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(path = "/addMember",
			method = RequestMethod.POST,
			consumes =  MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Create a new member")
	public ResponseEntity<Member> addMember(@RequestBody MemberView memberView) throws Exception {

		Member member = new Member();
		Member savedMember = new Member();

		// validate the input
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<MemberView>> violations = validator.validate(memberView);

		logger.error("size of violations : " + violations.size());

		for (ConstraintViolation<MemberView> constraintViolation : violations) {
			logger.error("constraintViolation: field \"" + constraintViolation.getPropertyPath() + "\"," + constraintViolation.getMessage());
			throw new WebserviceException(ErrorEnum.REQUIRED_ELEMENT_MISSING, constraintViolation.getMessage());
		}
		// validation - End

		if(violations.size() == 0) {
			logger.info("Validation Success!");

			//set view to model to be saved
			member.setMemberId(memberView.getMemberId());
			member.setFullname(memberView.getFullname());
			
			try {
				logger.info("Saving Member");
				savedMember =  memberService.saveMember(member);
				logger.info("Member creation completed");
			}catch (Exception e) {
				logger.error("Error:- Unable to create member");
				throw new WebserviceException(ErrorEnum.SAVING_UNSUCCESSFUL, ErrorEnum.SAVING_UNSUCCESSFUL.getDescription());
			}
		}
		return new ResponseEntity<Member>(savedMember, HttpStatus.CREATED);
	}

	/**
	 * Updates the member
	 * 
	 * @param updateMemberView
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(path = "/updateMember/{memberId}",
			method = RequestMethod.PUT)
	@ApiOperation(value = "Update a member")
	public ResponseEntity<Member> updateMember(@PathVariable String memberId, @RequestBody MemberView updateMemberView) throws Exception {

		Member savedMember = new Member();
		logger.info("Member no: "+memberId);
		Member memberFindByMemberId = memberService.findByMemberId(memberId);
		logger.info("Selected prod code: "+memberFindByMemberId.getMemberId());
		// validate the input
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<MemberView>> violations = validator.validate(updateMemberView);

		logger.error("size of violations : " + violations.size());

		for (ConstraintViolation<MemberView> constraintViolation : violations) {
			logger.error("constraintViolation: field \"" + constraintViolation.getPropertyPath() + "\"," + constraintViolation.getMessage());
			throw new WebserviceException(ErrorEnum.REQUIRED_ELEMENT_MISSING, constraintViolation.getMessage());
		}
		// validation - End

		if(violations.size() == 0) {
			logger.info("Validation Success!");

			logger.info("Selected Member id: "+memberFindByMemberId.getMemberId());
			memberFindByMemberId.setMemberId(updateMemberView.getMemberId());
			memberFindByMemberId.setFullname(updateMemberView.getFullname());

			try {
				logger.info("Update Member");
				savedMember = memberService.updateMember(memberFindByMemberId);
				logger.info("Member updated");
			}catch (Exception e) {
				logger.error("Error:- Unable to update member");
				throw new WebserviceException(ErrorEnum.SAVING_UNSUCCESSFUL, ErrorEnum.SAVING_UNSUCCESSFUL.getDescription());
			}
		}
		
		return new ResponseEntity<Member>(savedMember, HttpStatus.OK);
	}


	/**
	 * Deletes member identified with <code>memberId</code>
	 * @param memberId
	 * @throws Exception 
	 */
	@RequestMapping(path = "/removeMember/{memberId}", 
			method = RequestMethod.DELETE)
	@ApiOperation(value = "Delete a member")
	public ResponseEntity<?> deleteMember(@PathVariable String memberId) throws Exception {

		Member memberFindByMemberId = memberService.findByMemberId(memberId);
		try {
			logger.info("Delete Member");
			memberService.deleteMember(memberFindByMemberId);
			logger.info("Member Deleted");
		}catch (Exception e) {
			logger.error("Error:- Unable to delete member");
			throw new WebserviceException(ErrorEnum.DELETION_UNSUCCESSFUL, ErrorEnum.DELETION_UNSUCCESSFUL.getDescription());
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
}

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
import posmy.interview.boot.model.Librarian;
import posmy.interview.boot.service.LibrarianService;
import posmy.interview.boot.view.LibrarianView;

/**
 * @author Aboy
 * @version 0.01
 */
@CrossOrigin
@RestController
@RequestMapping("/librarian")
public class LibrarianController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private LibrarianService librarianService;

	/**
	 * Fetch a list of librarians
	 * @return a list of librarians
	 * @throws Exception 
	 */
	@RequestMapping(path="/allLibrarians", method = RequestMethod.GET, produces = "application/json")
	@ApiOperation(value = "Fetch all librarian")
	public ResponseEntity<?> librarian() throws Exception {
		List<Librarian> librarian = (List<Librarian>) librarianService.findAllLibrarians();

		return new ResponseEntity<>(librarian, HttpStatus.OK);
	}

	/**
	 * Finds a librarian by <code>librarianId</code>
	 * 
	 * @param librarianId librarian's librarianId
	 * 
	 * @return the {@link Librarian} object
	 * @throws Exception 
	 */
	@RequestMapping(path = "/getLibrarianByLibrarianId/{librarianId}", 
			method = RequestMethod.GET)
	@ApiOperation(value = "Fetch a librarian")
	public ResponseEntity<?> librarian(@PathVariable String librarianId) throws Exception {
		Librarian librarian = new Librarian();
		librarian = librarianService.findByLibrarianId(librarianId);
		
		return new ResponseEntity<>(librarian, HttpStatus.OK);
	}

	/**
	 * Add a librarian
	 * 
	 * @param librarianView
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(path = "newLibrarian",
			method = RequestMethod.POST,
			consumes =  MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Create a new librarian")
	public ResponseEntity<Librarian> addLibrarian(@RequestBody LibrarianView librarianView) throws Exception {

		Librarian librarian = new Librarian();
		Librarian savedLibrarian = new Librarian();

		// validate the input
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<LibrarianView>> violations = validator.validate(librarianView);

		logger.error("size of violations : " + violations.size());

		for (ConstraintViolation<LibrarianView> constraintViolation : violations) {
			logger.error("constraintViolation: field \"" + constraintViolation.getPropertyPath() + "\"," + constraintViolation.getMessage());
			throw new WebserviceException(ErrorEnum.REQUIRED_ELEMENT_MISSING, constraintViolation.getMessage());
		}
		// validation - End

		if(violations.size() == 0) {
			logger.info("Validation Success!");

			//set view to model to be saved
			librarian.setLibrarianId(librarianView.getLibrarianId());
			librarian.setFullname(librarianView.getFullname());
			librarian.setSalary(librarianView.getSalary());
			
			try {
				logger.info("Saving Librarian");
				savedLibrarian =  librarianService.saveLibrarian(librarian);
				logger.info("Librarian creation completed");
			}catch (Exception e) {
				logger.error("Error:- Unable to create librarian");
				throw new WebserviceException(ErrorEnum.SAVING_UNSUCCESSFUL, ErrorEnum.SAVING_UNSUCCESSFUL.getDescription());
			}
		}
		return new ResponseEntity<Librarian>(savedLibrarian, HttpStatus.CREATED);
	}

	/**
	 * Updates the librarian
	 * 
	 * @param updateLibrarianView
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(path = "/updateLibrarian/{librarianId}",
			method = RequestMethod.PUT)
	@ApiOperation(value = "Update a librarian")
	public ResponseEntity<Librarian> updateLibrarian(@PathVariable String librarianId, @RequestBody LibrarianView updateLibrarianView) throws Exception {

		Librarian savedLibrarian = new Librarian();
		logger.info("Librarian no: "+librarianId);
		Librarian librarianFindByLibrarianId = librarianService.findByLibrarianId(librarianId);
		logger.info("Selected LibrarianId: "+librarianFindByLibrarianId.getLibrarianId());
		// validate the input
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<LibrarianView>> violations = validator.validate(updateLibrarianView);

		logger.error("size of violations : " + violations.size());

		for (ConstraintViolation<LibrarianView> constraintViolation : violations) {
			logger.error("constraintViolation: field \"" + constraintViolation.getPropertyPath() + "\"," + constraintViolation.getMessage());
			throw new WebserviceException(ErrorEnum.REQUIRED_ELEMENT_MISSING, constraintViolation.getMessage());
		}
		// validation - End

		if(violations.size() == 0) {
			logger.info("Validation Success!");

			logger.info("Selected Librarian id: "+librarianFindByLibrarianId.getLibrarianId());
			librarianFindByLibrarianId.setLibrarianId(updateLibrarianView.getLibrarianId());
			librarianFindByLibrarianId.setFullname(updateLibrarianView.getFullname());
			librarianFindByLibrarianId.setSalary(updateLibrarianView.getSalary());
			
			try {
				logger.info("Update Librarian");
				savedLibrarian = librarianService.updateLibrarian(librarianFindByLibrarianId);
				logger.info("Librarian updated");
			}catch (Exception e) {
				logger.error("Error:- Unable to update librarian");
				throw new WebserviceException(ErrorEnum.SAVING_UNSUCCESSFUL, ErrorEnum.SAVING_UNSUCCESSFUL.getDescription());
			}
		}
		
		return new ResponseEntity<Librarian>(savedLibrarian, HttpStatus.OK);
	}


	/**
	 * Deletes librarian identified with <code>librarianId</code>
	 * @param librarianId
	 * @throws Exception 
	 */
	@RequestMapping(path = "/removeLibrarian/{librarianId}", 
			method = RequestMethod.DELETE)
	@ApiOperation(value = "Delete a librarian")
	public ResponseEntity<?> deleteLibrarian(@PathVariable String librarianId) throws Exception {

		Librarian librarianFindByLibrarianId = librarianService.findByLibrarianId(librarianId);
		try {
			logger.info("Delete Librarian");
			librarianService.deleteLibrarian(librarianFindByLibrarianId);
			logger.info("Librarian Deleted");
		}catch (Exception e) {
			logger.error("Error:- Unable to delete librarian");
			throw new WebserviceException(ErrorEnum.DELETION_UNSUCCESSFUL, ErrorEnum.DELETION_UNSUCCESSFUL.getDescription());
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
}

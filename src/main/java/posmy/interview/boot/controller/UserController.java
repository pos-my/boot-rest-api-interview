package posmy.interview.boot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import posmy.interview.boot.constant.Constants;
import posmy.interview.boot.exception.InvalidArgumentException;
import posmy.interview.boot.exception.InvalidPaginationException;
import posmy.interview.boot.exception.UnauthorisedException;
import posmy.interview.boot.model.user.*;
import posmy.interview.boot.service.UserService;
import posmy.interview.boot.util.Json;
import posmy.interview.boot.util.PaginationUtil;
import posmy.interview.boot.util.ValidationUtil;

;

@RestController
public class UserController implements UserOperations {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @Override
    public RegistrationResponse createUser(RegistrationRequest registrationRequest) {
        try {
            validateRegistrationRequest(registrationRequest);
            RegistrationResponse registrationResponse = userService.processRegistrationRequest(registrationRequest.getUsername(), registrationRequest.getFullName(),
                    registrationRequest.getPassword(), registrationRequest.getRole());
            logger.info("Successfully complete registration request, output = {}", Json.toString(registrationResponse));
            return registrationResponse;
        } catch (InvalidArgumentException e){
            logger.error("error", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid argument");
        }
    }

    @Override
    public GetUserResponse getUser(Integer pageSize, Integer pageNumber, String fullName, String username, Integer id) {
        try {
            PaginationUtil.validatePagination(pageNumber, pageSize);
            GetUserResponse getUserResponse = userService.getUsers(pageSize, pageNumber, fullName, username, id);
            return getUserResponse;
        } catch (InvalidPaginationException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid page number or size");
        }
    }

    @Override
    public UpdateUserResponse updateUser(UpdateUserRequest updateUserRequest) {
        try {
            UpdateUserResponse updateUserResponse = userService.updateUserRequest(updateUserRequest);
            return updateUserResponse;
        } catch (UnauthorisedException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unauthorised access");
        }
    }

    public void validateRegistrationRequest(RegistrationRequest registrationRequest) throws InvalidArgumentException{
        if (ValidationUtil.isStringEmpty(registrationRequest.getFullName())){
            throw new InvalidArgumentException();
        }

        if (ValidationUtil.isStringEmpty(registrationRequest.getUsername())){
            throw new InvalidArgumentException();
        }

        if (ValidationUtil.isStringEmpty(registrationRequest.getPassword())){
            throw new InvalidArgumentException();
        }

        if (ValidationUtil.isStringEmpty(registrationRequest.getRole()) || !Constants.Role.isRoleValid(registrationRequest.getRole())){
            throw new InvalidArgumentException();
        }
    }
}

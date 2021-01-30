package posmy.interview.boot.service.user;

import posmy.interview.boot.entity.UsersEntity;
import posmy.interview.boot.exception.LmsException;
import posmy.interview.boot.exception.NoDataFoundException;
import posmy.interview.boot.model.CreateUserRequest;
import posmy.interview.boot.model.UpdateUserRequest;
import posmy.interview.boot.model.User;

public interface UserService {
    UsersEntity registerNewUser(CreateUserRequest createUserRequest) throws LmsException;
    UsersEntity getUser();
    UsersEntity getUser(long userId) throws NoDataFoundException;
    UsersEntity deleteAccount() throws NoDataFoundException;
    UsersEntity deleteMemberAccount(long userId) throws NoDataFoundException, LmsException;
    User getMemberDetails(long userId) throws NoDataFoundException, LmsException;
    User updateMemberDetails(UpdateUserRequest updateUserRequest) throws NoDataFoundException, LmsException;
}

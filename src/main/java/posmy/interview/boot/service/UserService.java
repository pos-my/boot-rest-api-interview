package posmy.interview.boot.service;

import posmy.interview.boot.model.user.GetUserResponse;
import posmy.interview.boot.model.user.RegistrationResponse;
import posmy.interview.boot.model.user.UpdateUserRequest;
import posmy.interview.boot.model.user.UpdateUserResponse;

public interface UserService {
    RegistrationResponse processRegistrationRequest(String username, String fullname,
                                                    String password, String role);

    GetUserResponse getUsers(int pageSize, int pageNumber, String username, String fullName, Integer userId);

    UpdateUserResponse updateUserRequest(UpdateUserRequest updateUserRequest);
}

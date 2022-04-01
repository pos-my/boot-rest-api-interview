package posmy.interview.boot.user;

import java.util.List;

import posmy.interview.boot.entities.User;
import posmy.interview.boot.exceptions.DuplicateRecordException;
import posmy.interview.boot.exceptions.RecordNotFoundException;
import posmy.interview.boot.exceptions.UnauthorizedException;
import posmy.interview.boot.user.request.NewUserRequest;
import posmy.interview.boot.user.request.UpdateUserRequest;

public interface UserService {
    void delete(Long id) throws RecordNotFoundException, UnauthorizedException;

    List<User> viewAll() throws UnauthorizedException;

    User view(Long id) throws RecordNotFoundException, UnauthorizedException;

    User viewByName(String name) throws RecordNotFoundException, UnauthorizedException;

    User add(NewUserRequest userRequest) throws DuplicateRecordException;

    User update(Long id, UpdateUserRequest userRequest) throws RecordNotFoundException, UnauthorizedException;

}
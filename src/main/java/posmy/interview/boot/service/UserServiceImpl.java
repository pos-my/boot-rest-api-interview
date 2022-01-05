package posmy.interview.boot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import posmy.interview.boot.constant.Constants;
import posmy.interview.boot.database.UserDao;
import posmy.interview.boot.exception.InvalidArgumentException;
import posmy.interview.boot.exception.UnauthorisedException;
import posmy.interview.boot.model.book.Book;
import posmy.interview.boot.model.book.UpdateBookResponse;
import posmy.interview.boot.model.common.Pagination;
import posmy.interview.boot.model.database.BookEntity;
import posmy.interview.boot.model.security.CustomUserDetail;
import posmy.interview.boot.model.user.*;
import posmy.interview.boot.model.database.UserEntity;
import posmy.interview.boot.util.DateUtil;
import posmy.interview.boot.util.PaginationUtil;
import posmy.interview.boot.util.ValidationUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UserServiceImpl implements UserService {

    UserDao userDao;
    PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder){
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public RegistrationResponse processRegistrationRequest(String username, String fullname, String password, String role) {
        RegistrationResponse registrationResponse = new RegistrationResponse();
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(username);
        userEntity.setFullName(fullname);
        userEntity.setUserPassword(passwordEncoder.encode(password));
        userEntity.setUserRole(role);
        userEntity.setStatus(Constants.UserStatus.ACTIVATED.getType());

        Calendar currentDateTime = Calendar.getInstance();
        userEntity.setRecordCreateDate(new Timestamp(currentDateTime.getTime().getTime()));
        userEntity.setRecordUpdateDate(new Timestamp(currentDateTime.getTime().getTime()));

        UserEntity result = userDao.save(userEntity);
        registrationResponse.setId(result.getUserId());
        registrationResponse.setUsername(result.getUserName());
        registrationResponse.setFullName(result.getFullName());
        registrationResponse.setRole(result.getUserRole());
        registrationResponse.setStatus(result.getStatus());
        registrationResponse.setRecordCreateDate(DateUtil.convertToStandardDateStringFormat(userEntity.getRecordCreateDate().getTime()));
        registrationResponse.setRecordUpdateDate(DateUtil.convertToStandardDateStringFormat(userEntity.getRecordUpdateDate().getTime()));
        return registrationResponse;
    }

    @Override
    public GetUserResponse getUsers(int pageSize, int pageNumber, String username, String fullName, Integer userId) {
        pageNumber = pageNumber - 1;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        username = reformatQueryString(username);
        fullName = reformatQueryString(fullName);

        Page<UserEntity> userEntities = userDao.getUserEntitiesByUserNameAndFullNameAndUserId(username, fullName, userId, pageable);

        List<User> users = new ArrayList<>();
        for (UserEntity userEntity: userEntities){
            User user = new User();
            user.setUsername(userEntity.getUserName());
            user.setFullName(userEntity.getFullName());
            user.setRole(userEntity.getUserRole());
            user.setStatus(userEntity.getStatus());
            user.setRecordCreateDate(DateUtil.convertToStandardDateStringFormat(userEntity.getRecordCreateDate().getTime()));
            user.setRecordUpdateDate(DateUtil.convertToStandardDateStringFormat(userEntity.getRecordUpdateDate().getTime()));
            users.add(user);
        }

        GetUserResponse getUserResponse = new GetUserResponse();
        getUserResponse.setRecords(users);
        Pagination pagination = PaginationUtil.createPagination((int)userEntities.getTotalElements(), pageSize, pageNumber);
        getUserResponse.setPagination(pagination);
        return getUserResponse;
    }

    @Override
    public UpdateUserResponse updateUserRequest(UpdateUserRequest updateUserRequest){
        UserEntity userEntity = userDao.findUserEntityByUserId(updateUserRequest.getUserId());
        checkIfUserHasAuthority(updateUserRequest.getUserId(), updateUserRequest.getStatus());
        if (userEntity == null){
            throw new InvalidArgumentException();
        }

        if (!ValidationUtil.isStringEmpty(updateUserRequest.getFullName())){
            userEntity.setFullName(updateUserRequest.getFullName());
        }

        if (!ValidationUtil.isStringEmpty(updateUserRequest.getStatus())){
            userEntity.setStatus(updateUserRequest.getStatus());
        }

        if (!ValidationUtil.isStringEmpty(updateUserRequest.getRole())){
            userEntity.setUserRole(updateUserRequest.getRole());
        }

        return updateUserInDb(userEntity);
    }

    private void checkIfUserHasAuthority(Integer id, String status) throws UnauthorisedException {
        String role = getCurrentUserRole();
        if (role.equals(Constants.ROLE_LIBRARIAN) ){
            return;
        } else if (role.equals(Constants.ROLE_MEMBER) &&
                (status.equals(Constants.UserStatus.REMOVED.getType()))){

            //member role can only remove their own account
            UserEntity userEntity = userDao.findUserEntityByUserId(id);
            if (!userEntity.getUserName().equals(getCurrentUserName())){
                throw new UnauthorisedException();
            }
            return;
        }
        throw new UnauthorisedException();
    }

    private UpdateUserResponse updateUserInDb(UserEntity userEntity) {
        Calendar currentDateTime = Calendar.getInstance();
        userEntity.setRecordUpdateDate(new Timestamp(currentDateTime.getTime().getTime()));
        UserEntity result = userDao.save(userEntity);
        UpdateUserResponse updateUserResponse = new UpdateUserResponse();
        updateUserResponse.setUserId(result.getUserId());
        updateUserResponse.setFullName(result.getFullName());
        updateUserResponse.setUsername(result.getUserName());
        updateUserResponse.setRole(result.getUserRole());
        updateUserResponse.setStatus(result.getStatus());
        updateUserResponse.setRecordCreateDate(DateUtil.convertToStandardDateStringFormat(result.getRecordCreateDate().getTime()));
        updateUserResponse.setRecordUpdateDate(DateUtil.convertToStandardDateStringFormat(result.getRecordUpdateDate().getTime()));
        return updateUserResponse;
    }

    private String reformatQueryString(String input){
        if (input==null || input.replace(" ", "").length() == 0){
            return null;
        } else {
            return "%" + input + "%";
        }
    }

    private String getCurrentUserRole(){
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = "";
        for (GrantedAuthority authority : userDetail.getAuthorities()) {
            role = authority.getAuthority();
            break;
        }
        return role;
    }

    private String getCurrentUserName(){
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetail.getUsername();
    }
}

package posmy.interview.boot.services.user;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import posmy.interview.boot.db.UserDal;
import posmy.interview.boot.helper.DateHelper;
import posmy.interview.boot.model.auth.UserAuthentication;
import posmy.interview.boot.model.entity.UserEntity;
import posmy.interview.boot.model.request.UserRequest;
import posmy.interview.boot.model.response.UserResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService  {

    //TODO: hide password

    @Autowired
    private UserDal userDal;

    @Override
    public void remove(String id) {
        Optional<UserEntity> userEntity = this.userDal.findById(id);

        if(userEntity.isEmpty()){
            throw new ObjectNotFoundException("User with ID = {} cannot be found", id);
        }

        this.userDal.deleteById(id);
    }

    @Override
    public List<UserResponse> viewAll() {
        List<UserResponse> userResponseList = new ArrayList<>();
        this.userDal.findAll().forEach(val ->
                userResponseList.add(
                        new UserResponse(
                                val.getUserId(),
                                val.getUserName(),
                                val.getEmail(),
                                val.getPassowrd(),
                                val.getRole(),
                                val.getStatus(),
                                DateHelper.displayDate(val.getDateCreated()),
                                DateHelper.displayDate(val.getDateUpdated())
                        )
                )
        );
        return userResponseList;
    }

    @Override
    public UserResponse view(String id) {
        Optional<UserEntity> userEntity = this.userDal.findById(id);

        if(userEntity.isEmpty()){
            throw new ObjectNotFoundException("User with ID = {} cannot be found", id);
        }

        UserEntity user = userEntity.get();

        //Return
        return new UserResponse(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getPassowrd(),
                user.getRole(),
                user.getStatus(),
                DateHelper.displayDate(user.getDateCreated()),
                DateHelper.displayDate(user.getDateUpdated())
        );
    }

    @Override
    public UserResponse save(UserRequest userRequest) {
        UserEntity user = new UserEntity();
        user.setUserName(userRequest.getUserName());
        user.setEmail(userRequest.getEmail());
        user.setPassowrd(userRequest.getPassword());
        user.setRole(userRequest.getRole());
        user.setStatus("ACTIVE");
        user.setDateCreated(new Date());
        user.setDateUpdated(new Date());
        this.userDal.save(user);

        //Return
        return new UserResponse(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getPassowrd(),
                user.getRole(),
                user.getStatus(),
                DateHelper.displayDate(user.getDateCreated()),
                DateHelper.displayDate(user.getDateUpdated())
        );
    }

    @Override
    public UserResponse update(UserRequest userRequest) {
        Optional<UserEntity> userEntity = this.userDal.findById(userRequest.getId());

        if(userEntity.isEmpty()){
            throw new ObjectNotFoundException("User with ID = {} cannot be found", userRequest.getId());
        }

        //Save
        UserEntity user = userEntity.get();
        user.setUserName(userRequest.getUserName());
        user.setEmail(userRequest.getEmail());
        user.setPassowrd(userRequest.getPassword());
        user.setRole(userRequest.getRole());
        if(user.getStatus() != null && !user.getStatus().isEmpty()) {
            user.setStatus(userRequest.getStatus());
        }
        user.setDateUpdated(new Date());
        this.userDal.save(user);

        //Return
        return new UserResponse(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getPassowrd(),
                user.getRole(),
                user.getStatus(),
                DateHelper.displayDate(user.getDateCreated()),
                DateHelper.displayDate(user.getDateUpdated())
        );
    }
}

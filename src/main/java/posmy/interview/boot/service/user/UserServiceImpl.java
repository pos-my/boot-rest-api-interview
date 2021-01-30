package posmy.interview.boot.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import posmy.interview.boot.entity.RolesEntity;
import posmy.interview.boot.entity.UsersEntity;
import posmy.interview.boot.exception.LmsException;
import posmy.interview.boot.exception.NoDataFoundException;
import posmy.interview.boot.model.CreateUserRequest;
import posmy.interview.boot.model.UpdateUserRequest;
import posmy.interview.boot.model.User;
import posmy.interview.boot.repository.RolesRepository;
import posmy.interview.boot.repository.UsersRepository;
import posmy.interview.boot.util.AuthUtils;
import posmy.interview.boot.util.RoleUtils;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UsersEntity registerNewUser(CreateUserRequest createUserRequest) throws LmsException {
        UsersEntity usersEntity = usersRepository.findByUsername(createUserRequest.getUsername());
        if(usersEntity != null){
            throw new LmsException("Username already taken");
        }
        UsersEntity newUser = modelMapper.map(createUserRequest, UsersEntity.class);
        newUser.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
        RolesEntity memberRole = rolesRepository.findByName(createUserRequest.getRoles().name());
        newUser.setRoles(Set.of(memberRole));
        usersRepository.save(newUser);
        return newUser;
    }

    @Override
    public UsersEntity getUser() {
        return usersRepository.findByUsername(AuthUtils.getUsername());
    }

    @Override
    public UsersEntity getUser(long userId) throws NoDataFoundException {
        return usersRepository.findById(userId).orElseThrow(() -> new NoDataFoundException("There is no such user"));
    }

    @Override
    public UsersEntity deleteAccount() throws NoDataFoundException {
        UsersEntity usersEntity = getUser();
        if(null == usersEntity) {
            throw new NoDataFoundException("No user account found");
        }
        usersRepository.delete(usersEntity);
        return usersEntity;
    }

    @Override
    public UsersEntity deleteMemberAccount(long userId) throws NoDataFoundException, LmsException {
        UsersEntity usersEntity = getUser(userId);
        log.info("To delete {}", usersEntity);
        Set<RolesEntity> roles = usersEntity.getRoles();
        if(CollectionUtils.isEmpty(roles)) {
            throw new NoDataFoundException("There are no roles");
        }
        if(RoleUtils.isNotMember(usersEntity))  {
            throw new LmsException("User is not a member. Unable to delete account");
        }
        usersRepository.delete(usersEntity);
        return usersEntity;
    }

    @Override
    public User getMemberDetails(long userId) throws NoDataFoundException, LmsException {
        UsersEntity usersEntity = getUser(userId);
        if(RoleUtils.isNotMember(usersEntity))  {
            throw new LmsException("No access to view account details");
        }
        return modelMapper.map(usersEntity, User.class);
    }

    @Override
    public User updateMemberDetails(UpdateUserRequest updateUserRequest) throws NoDataFoundException, LmsException {
        UsersEntity usersEntity = getUser(updateUserRequest.getUserId());
        if(RoleUtils.isNotMember(usersEntity))  {
            throw new LmsException("No access to update account details");
        }
        usersEntity.setFirstName(updateUserRequest.getFirstName());
        usersEntity.setLastName(updateUserRequest.getLastName());
        usersEntity.setEmail(updateUserRequest.getEmail());
        usersRepository.save(usersEntity);
        return modelMapper.map(usersEntity, User.class);
    }
}

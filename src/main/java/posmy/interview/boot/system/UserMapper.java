package posmy.interview.boot.system;

import org.modelmapper.ConfigurationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import posmy.interview.boot.dto.UserDto;
import posmy.interview.boot.model.User;

@Component
public class UserMapper implements Mapper<UserDto, User> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDto convertToDto(User model) {
        UserDto dto;
        try {
            dto = modelMapper.map(model, UserDto.class);
        } catch (ConfigurationException ce) {
            dto = new UserDto();
        }
        return dto;
    }

    @Override
    public User convertToModel(UserDto dto) {
        User user;
        try {
            user = modelMapper.map(dto, User.class);
        } catch (ConfigurationException ce) {
            user = new User();
        }
        return user;
    }
}

package posmy.interview.boot.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import posmy.interview.boot.common.enums.RoleEnums;
import posmy.interview.boot.entities.User;
import org.modelmapper.ModelMapper;

public abstract class BaseSecurityService {

    @Autowired
    private CurrentSession session;

    @Autowired
    protected ModelMapper modelMapper = new ModelMapper();

    protected User getCurrentUser() {
        return this.session.getCurrentUser();
    }

    protected boolean isLibrarian() {
        return StringUtils.equalsIgnoreCase(this.getCurrentUser().getRole().getName().toString(),
                RoleEnums.LIBRARIAN.toString());
    }

}

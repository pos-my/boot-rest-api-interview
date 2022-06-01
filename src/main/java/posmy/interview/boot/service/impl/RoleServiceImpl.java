package posmy.interview.boot.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import posmy.interview.boot.model.Role;
import posmy.interview.boot.repository.RoleRepo;
import posmy.interview.boot.service.api.RoleService;
import posmy.interview.boot.snowflake.SnowflakeHelper;

import java.util.List;

@Service
@Transactional
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepo roleRepo;

    @Override
    public List<Role> saveAll(List<Role> roles) {
        log.info("save roles: {}", roles);
        //set id when it is new role insert
        SnowflakeHelper.assignLongIds(roles);
        //save and return the saved data
        return roleRepo.saveAll(roles);
    }

    @Override
    public List<Role> findAll() {
        log.info("find all roles");
        return roleRepo.findAll();
    }

    //todo: create BASE service and implement abstract saveValidate + preAssign
    // , so others standard service can extend it and minimize the general code such as saveAll in future

}

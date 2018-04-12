package com.app.service;

import com.app.entity.Role;
import com.app.entity.enums.RoleTypeEnum;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.List;

@Service
public interface RoleService {
    Role save(Role role);
    void delete(Long id) throws RoleNotFoundException;
    List<Role> findAll();
    Role findById(Long id) throws RoleNotFoundException;
    Role findByRoleType(RoleTypeEnum roleTypeEnum) throws RoleNotFoundException;
}

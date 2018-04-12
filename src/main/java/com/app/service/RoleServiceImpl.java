package com.app.service;

import com.app.entity.Role;
import com.app.entity.enums.RoleTypeEnum;
import com.app.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void delete(Long id) throws RoleNotFoundException {
        if (roleRepository.exists(id)) {
            roleRepository.delete(id);
        } else {
            throw new RoleNotFoundException();
        }
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Role findById(Long id) throws RoleNotFoundException {
        if (roleRepository.exists(id)) {
            return roleRepository.findOne(id);
        } else {
            throw new RoleNotFoundException();
        }
    }

    @Override
    public Role findByRoleType(RoleTypeEnum roleTypeEnum) throws RoleNotFoundException {
        Role role = roleRepository.findByRoleType(roleTypeEnum);
        if(role == null) {
            throw new RoleNotFoundException();
        } else {
            return role;
        }
    }
}

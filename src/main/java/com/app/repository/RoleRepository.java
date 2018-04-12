package com.app.repository;

import com.app.entity.Role;
import com.app.entity.enums.RoleTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleType(RoleTypeEnum roleType);
}

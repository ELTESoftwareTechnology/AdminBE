package com.app.entity;

import com.app.entity.enums.RoleTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleTypeEnum roleType;

    public Role(RoleTypeEnum roleType) {
        this.roleType = roleType;
    }

}

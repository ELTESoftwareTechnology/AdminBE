package com.app.security.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class  JwtAuthenticationRequest implements Serializable {

    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String roleType;

}

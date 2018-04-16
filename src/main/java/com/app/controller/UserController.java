package com.app.controller;

import com.app.entity.User;
import com.app.entity.enums.RoleTypeEnum;
import com.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {

    public final static String LIST_DOCTOR_URL = "/api/doctor/list";
    private UserService userService;

    /**
     * Injects UserService instance
     * @param userService to inject
     */
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(LIST_DOCTOR_URL)
    public ResponseEntity listDoctors(){
        // TODO: filter in query instead
        List<User> users = this.userService.findAll();
        users = users.stream().filter(user -> user.getRole().getRoleType().equals(RoleTypeEnum.DOCTOR)).collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

}

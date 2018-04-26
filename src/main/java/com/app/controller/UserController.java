package com.app.controller;

import com.app.entity.ChunkInfo;
import com.app.entity.User;
import com.app.entity.enums.RoleTypeEnum;
import com.app.security.auth.JwtUser;
import com.app.service.ChunkService;
import com.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController extends BaseController {

    public final static String LIST_DOCTOR_URL = "/api/doctor/list";
    public final static String LIST_PATIENT_URL = "/api/patient/list";

    private ChunkService chunkService;
    private UserService userService;

    /**
     * Injects ChunkService instance
     * @param chunkService to inject
     */
    @Autowired
    public void setChunkService(ChunkService chunkService) {
        this.chunkService = chunkService;
    }

    /**
     * Injects UserService instance
     * @param userService to inject
     */
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Returns all registered doctors
     * @return Doctor list
     */
    @GetMapping(LIST_DOCTOR_URL)
    public ResponseEntity listDoctors(){
        // TODO: filter in query instead
        List<User> users = this.userService.findAll();
        users = users.stream().filter(user -> user.getRole().getRoleType().equals(RoleTypeEnum.DOCTOR)).collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    /**
     * Returns all patients for a logged in doctor (the users who sent them data)
     * @return Patient list
     */
    @GetMapping(LIST_PATIENT_URL)
    public ResponseEntity getPatients(){
        User currentUser = this.userService.findByUsername(((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        List<User> patients = chunkService.findChunksForUser(currentUser).stream().map(ChunkInfo::getFrom).distinct().collect(Collectors.toList());;
        return ResponseEntity.ok(patients);
    }
}

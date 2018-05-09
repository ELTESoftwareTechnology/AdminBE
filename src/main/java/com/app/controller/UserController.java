package com.app.controller;

import com.app.entity.ChunkInfo;
import com.app.entity.User;
import com.app.entity.enums.RoleTypeEnum;
import com.app.notification.InvitationRequest;
import com.app.notification.NotificationManager;
import com.app.security.auth.JwtUser;
import com.app.service.ChunkService;
import com.app.service.UserService;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController extends BaseController {

    private final static String LIST_DOCTOR_URL = "/api/doctor/list";
    private final static String LIST_PATIENT_URL = "/api/patient/list";
    private final static String INVITE_DOCTOR_URL = "/api/doctor/invite";

    private ChunkService chunkService;
    private UserService userService;
    private NotificationManager notificationManager;

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
     * Injects NotificationManager instance
     * @param notificationManager to inject
     */
    @Autowired
    public void setNotificationManager(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
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

    @PostMapping(INVITE_DOCTOR_URL)
    public ResponseEntity inviteDoctor(@Valid @RequestBody InvitationRequest invitationRequest){
        User currentUser = this.userService.findByUsername(((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        notificationManager.sendMail(NotificationManager.NotificationType.Invitation, currentUser, invitationRequest.getTargetEmail(), null);
        return new ResponseEntity(HttpStatus.OK);
    }
}

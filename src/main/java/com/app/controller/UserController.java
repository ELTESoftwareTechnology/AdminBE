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
public class UserController extends BaseController {

    public final static String LIST_DOCTOR_URL = "/api/doctor/list";
    public final static String LIST_PATIENT_URL = "/api/patient/list";
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

    @GetMapping(LIST_PATIENT_URL)
    public ResponseEntity getPatients(){
        String dummyJson = "{\n" +
                "  \"patients\": [\n" +
                "    {\n" +
                "      \"id\": 5,\n" +
                "      \"username\": \"test5\",\n" +
                "      \"email\": \"test5@t.com\",\n" +
                "      \"firstName\": \"John\",\n" +
                "      \"lastName\": \"McClane\",\n" +
                "      \"role\": {\n" +
                "        \"id\": 2,\n" +
                "        \"roleType\": \"USER\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 6,\n" +
                "      \"username\": \"test6\",\n" +
                "      \"email\": \"test6@t.com\",\n" +
                "      \"firstName\": \"Korben\",\n" +
                "      \"lastName\": \"Dallas\",\n" +
                "      \"role\": {\n" +
                "        \"id\": 2,\n" +
                "        \"roleType\": \"USER\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 7,\n" +
                "      \"username\": \"test7\",\n" +
                "      \"email\": \"test7@t.com\",\n" +
                "      \"firstName\": \"Butch\",\n" +
                "      \"lastName\": \"Coolidge\",\n" +
                "      \"role\": {\n" +
                "        \"id\": 2,\n" +
                "        \"roleType\": \"USER\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        return ResponseEntity.ok(dummyJson);
    }
}

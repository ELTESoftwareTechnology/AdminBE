package com.app.controller;

import com.app.entity.Role;
import com.app.entity.enums.RoleTypeEnum;
import com.app.entity.User;
import com.app.exception.InvalidPasswordException;
import com.app.exception.UserAlreadyExistsException;
import com.app.exception.UserNotFoundException;
import com.app.security.auth.JwtAuthenticationRequest;
import com.app.security.auth.JwtAuthenticationResponse;
import com.app.security.auth.JwtUtil;
import com.app.security.auth.JwtUser;
import com.app.service.RoleService;
import com.app.service.UserService;
import com.app.util.ExceptionUtil;
import com.virgilsecurity.sdk.crypto.VirgilCrypto;
import com.virgilsecurity.sdk.crypto.VirgilKeyPair;
import com.virgilsecurity.sdk.utils.ConvertionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.RollbackException;
import javax.servlet.http.HttpServletRequest;

/**
 * AuthController provides signup, signin and token refresh methods
 * @author saka7
 */
@RestController
public class AuthController extends BaseController {

    @Value("${auth.header}")
    private String tokenHeader;

    public final static String SIGNUP_URL = "/api/auth/signup";
    public final static String SIGNIN_URL = "/api/auth/signin";
    public final static String REFRESH_TOKEN_URL = "/api/auth/token/refresh";

    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;
    private UserDetailsService userDetailsService;
    private UserService userService;
    private RoleService roleService;

    /**
     * Injects AuthenticationManager instance
     * @param authenticationManager to inject
     */
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * Injects JwtUtil instance
     * @param jwtUtil to inject
     */
    @Autowired
    public void setJwtTokenUtil(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Injects UserDetailsService instance
     * @param userDetailsService to inject
     */
    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Injects UserService instance
     * @param userService to inject
     */
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) { this.roleService = roleService; }

    @Bean
    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Adds new user and returns authentication token
     * @param authenticationRequest request with username, email and password fields
     * @return generated JWT
     * @throws AuthenticationException
     */
    @PostMapping(SIGNUP_URL)
    public ResponseEntity createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest)
            throws AuthenticationException {

        final String name = authenticationRequest.getUsername();
        final String email = authenticationRequest.getEmail();
        final String password = authenticationRequest.getPassword();
        final String firstName = authenticationRequest.getFirstName();
        final String lastName = authenticationRequest.getLastName();
        RoleTypeEnum roleType;

        LOG.info("[POST] CREATING TOKEN FOR User " + name);

        try {
            roleType = RoleTypeEnum.valueOf(authenticationRequest.getRoleType());
        } catch (IllegalArgumentException | NullPointerException ex) {
            return new ResponseEntity<>("The selected roleType does not exist or parameter is missing.", HttpStatus.BAD_REQUEST);
        }

        if(this.userService.findByName(name) != null) {
           throw new UserAlreadyExistsException();
        }

        if(this.userService.findByEmail(email) != null) {
            throw new UserAlreadyExistsException();
        }

        JwtUser userDetails;

        try {
            //TODO: Replace this with actual role validation!
            Role role  = this.roleService.findByRoleType(roleType);

            VirgilCrypto crypto = new VirgilCrypto();
            VirgilKeyPair keyPair = crypto.generateKeys();
            byte[] privateKeyData = crypto.exportPrivateKey(keyPair.getPrivateKey(), password);
            String privateKeyStr = ConvertionUtils.toBase64String(privateKeyData);
            byte[] publicKeyData = crypto.exportPublicKey(keyPair.getPublicKey());
            String publicKeyStr = ConvertionUtils.toBase64String(publicKeyData);

            User user = new User(0L, name, email, password, privateKeyStr, publicKeyStr, firstName, lastName, role);
            LOG.info(user.toString());
            userService.save(user);
            userDetails = (JwtUser) userDetailsService.loadUserByUsername(name);
        } catch (UsernameNotFoundException ex) {
            LOG.error(ex.getMessage());
            throw new UserNotFoundException();
        } catch (RoleNotFoundException ex) {
            return new ResponseEntity<>("Role type does not exist.", HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
            if(ExceptionUtil.isCausedBy(RollbackException.class, ex)) {
                return new ResponseEntity<>(ex.getCause().getCause().getMessage(), HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }

        final Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(name, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }

    /**
     * Returns authentication token for given user
     * @param authenticationRequest with username and password
     * @return generated JWT
     * @throws AuthenticationException
     */
    @PostMapping(SIGNIN_URL)
    public ResponseEntity getAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest)
            throws AuthenticationException {

        final String name = authenticationRequest.getUsername();
        final String password = authenticationRequest.getPassword();
        LOG.info("[POST] GETTING TOKEN FOR User " + name);
        JwtUser userDetails;

        try {
            userDetails = (JwtUser) userDetailsService.loadUserByUsername(name);
        } catch (UsernameNotFoundException | NoResultException ex) {
            LOG.error(ex.getMessage());
            throw new UserNotFoundException();
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
            return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
        }

        if(!passwordEncoder().matches(password, userDetails.getPassword())) {
            throw new InvalidPasswordException();
        }

        final Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(name, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }

    /**
     * Refreshes token
     * @param request with old JWT
     * @return Refreshed JWT
     */
    @PostMapping(REFRESH_TOKEN_URL)
    public ResponseEntity refreshAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        LOG.info("[POST] REFRESHING TOKEN");
        String refreshedToken = jwtUtil.refreshToken(token);
        return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
    }

}

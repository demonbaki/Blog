package it.cgmconsulting.myblog.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.cgmconsulting.myblog.entity.Authority;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.security.UserPrincipal;
import it.cgmconsulting.myblog.service.AuthorityService;
import it.cgmconsulting.myblog.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("user")
@SecurityRequirement(name = "myBlogSecurityScheme")
public class UserController {

    private UserService userService;
    private AuthorityService authorityService;
    private PasswordEncoder passwordEncoder;

    public UserController(UserService userService, AuthorityService authorityService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.authorityService = authorityService;
        this.passwordEncoder = passwordEncoder;
    }

    @PutMapping("/change-role/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional//and hasRole('ROLE_MODERATOR')")
    public ResponseEntity<?> changeRole(@PathVariable long id, @RequestParam @NotEmpty Set<String> newAuthorities, @AuthenticationPrincipal UserPrincipal userPrincipal){

        if(userPrincipal.getId() == id)
            return new ResponseEntity<>("you cannot change your own auhotrities", HttpStatus.FORBIDDEN);

        Optional<User> u = userService.findById(id);

        if(!u.isPresent())
            return new ResponseEntity<>("user not found", HttpStatus.NOT_FOUND);
        Set<Authority> authorities = authorityService.findByAuthorityNameIn(newAuthorities);

        if (authorities.isEmpty())
            return new ResponseEntity<>("No authority selected", HttpStatus.NOT_FOUND);
        u.get().setAuthorities(authorities);

        return  new ResponseEntity<>("Authorities updated for user "+ u.get().getUsername(), HttpStatus.OK);

    }

    @PutMapping("/change-pwd")
    @Transactional
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal UserPrincipal userPrincipal,@RequestParam   @Pattern(regexp = "^[a-zA-Z0-9]{6,10}$",
            message = "password must of 6 to 10 lenght whit no special characters")String newPassword){

        Optional<User> u = userService.findById(userPrincipal.getId());
        if(!u.isPresent())
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        u.get().setPassword(passwordEncoder.encode(newPassword));
        userService.save(u.get());
        return new ResponseEntity<>("Password changed", HttpStatus.OK);

    }
}

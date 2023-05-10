package it.cgmconsulting.myblog.service;

import io.micrometer.observation.annotation.Observed;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.repository.AuthorityRepository;
import it.cgmconsulting.myblog.repository.UserRepository;
import it.cgmconsulting.myblog.request.SignUpRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class UserService{

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, AuthorityRepository authorityRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder=passwordEncoder;
    }

    public void save(User u) {
        userRepository.save(u);
    }

    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    public boolean existsByUsernameOrEmail(String username, String email) {
        return userRepository.existsByUsernameOrEmail(username, email);
    }

    public User fromSignUpRequestToEntity(SignUpRequest request){
        return new User(request.getUsername(), passwordEncoder.encode(request.getPassword()), request.getEmail());
    }


    public Optional<User> findByConfirmCode(String confirmCode){
        return userRepository.findByConfirmCode(confirmCode);
    }

    public Optional<User> findByUsernameOrEmail(String username, String email) { return userRepository.findByUsernameOrEmail(username,email); }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword))
            return false;
        return true;
    }

    public void updatePassword(@Param("id")long id, @Param("newPassword") String newPassword, @Param("now") LocalDateTime now){
        userRepository.updatePassword(id,newPassword,now);
    }

    public boolean existsByUsernameAndIdNot(String username,Long id){
        return userRepository.existsByUsernameAndIdNot(username,id);
    }

    public void updateUsername(long id, String newUsername){
        userRepository.updateUsername(id,newUsername,LocalDateTime.now());
    }




}

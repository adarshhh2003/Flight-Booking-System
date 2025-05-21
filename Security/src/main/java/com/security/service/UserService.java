package com.security.service;

import com.security.entity.LoginRequest;
import com.security.entity.Users;
import com.security.repository.UserRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private JwtService jwtService;

    private AuthenticationManager authManager;

    private UserRepo userRepo;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    public UserService(JwtService jwtService, AuthenticationManager authManager, UserRepo userRepo) {
        this.jwtService = jwtService;
        this.authManager = authManager;
        this.userRepo = userRepo;
    }

    public ResponseEntity<String> addUser(Users users) {
        users.setPassword(encoder.encode(users.getPassword()));
        userRepo.save(users);
        return ResponseEntity.ok("User Registered Successfully");
    }

    public ResponseEntity<String> updateUser(String email, Users users) {
        Optional<Users> oldUser = userRepo.findByEmail(email);

        if(oldUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email Id:  " + email);
        }

        BeanUtils.copyProperties(users, oldUser.get(), "id");
        oldUser.get().setPassword(encoder.encode(users.getPassword()));
        userRepo.save(oldUser.get());

        return ResponseEntity.ok("Profile Updated Successfully");
    }

    public Optional<Users> checkExistingUser(String email) {
        return userRepo.findByEmail(email);
    }

    public String verify(LoginRequest loginRequest) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        if(authentication.isAuthenticated()) {

            Optional<Users> usersOptional = userRepo.findByEmail(loginRequest.getEmail());

            if(usersOptional.isEmpty()) {
                return "User not found";
            }

            String role = usersOptional.get().getRole();

            return jwtService.generateToken(loginRequest.getEmail(), role);
        } else {
            return "Fail";
        }
    }
}

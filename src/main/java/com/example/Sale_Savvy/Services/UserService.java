package com.example.Sale_Savvy.Services;

import com.example.Sale_Savvy.Entities.Role;
import com.example.Sale_Savvy.Entities.User;
import com.example.Sale_Savvy.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder(); //BCrypt encoder object convert plain password into hashed password
    }

//    Receives a User object from Controller
    public User registerUser(User user) {

//        Check if username or email already exists or not
        if(userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already taken");
        }
        if(userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already registered");
        }

//        Encrypt Password means Suppose user enters: password = admin123, After encoding: password = $2a$10$8fR....
//        Now User object contains hashed password.
        user.setPassword(passwordEncoder.encode((user.getPassword())));
        user.setRole(Role.CUSTOMER);
//        Save the user
        return userRepository.save(user);
    }
}

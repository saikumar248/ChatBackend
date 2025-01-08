package com.chat.app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chat.app.exception.ResourceNotFound;
import com.chat.app.model.User;
import com.chat.app.service.UserService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class UserController {

    @Autowired
    private UserService userService;
    

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        User newUser = userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully!");
    }
    
    

    @GetMapping("/fetchall")
    public ResponseEntity<List<User>> fetchAll() {
        return userService.fetchAll();  
    }

    
    @GetMapping("/fetch")
    public ResponseEntity<List<User>> fetch() {
        return userService.fetchAll();  
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully!");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User user = userService.updateUser(id, updatedUser);
        return ResponseEntity.ok("User updated successfully!");
    }
    
    
    @GetMapping("/fetch/email/{email}")
    public ResponseEntity<User> fetchByEmail(@PathVariable String email) {
        User user = userService.fetchByEmail(email);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
        }
    }
    

    
    @PutMapping("/updateProfileByEmail/{email}")
    public ResponseEntity<String> updateUserByEmail(@PathVariable String email, @RequestBody User updatedUser) {
        User user = userService.updateUserByEmail(email, updatedUser);
        return ResponseEntity.ok("User updated successfully!");
    }
    
    @PutMapping("/updatePasswordByEmail")
    public ResponseEntity<String> updateUserPasswordByEmail(
            @RequestParam String email, 
            @RequestParam String newPassword) {
        userService.updateUserPasswordByEmail(email, newPassword);
        return ResponseEntity.ok("Password updated successfully!");
    }


    
//    @PostMapping("/signin")
//    public ResponseEntity<String> signin(@RequestBody User user) {
//        User loggedInUser = userService.loginUser(user.getEmail(), user.getPassword());
//        return ResponseEntity.ok("Signin successful!");  
//    }
    
    
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody User user) {
        try {
            // Attempt to log in the user
            User loggedInUser = userService.loginUser(user.getEmail(), user.getPassword());

            // Create a response map
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Signin successful!");
            response.put("user", loggedInUser);

            return ResponseEntity.ok(response);

        } catch (ResourceNotFound e) {
            // Handle invalid credentials
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
        } catch (Exception e) {
            // Handle other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("An error occurred during signin.");
        }
    }

}

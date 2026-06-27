package com.example.Sale_Savvy.Controller;


import com.example.Sale_Savvy.DTO.LoginRequest;
import com.example.Sale_Savvy.Entities.User;
import com.example.Sale_Savvy.Services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(
        origins = {
                "http://localhost:5173",
                "https://sale-savvy-frontend.vercel.app"
        },
        allowCredentials = "true"
)
@RequestMapping("/api/auth")

public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;

    }
        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
            try {
                User user = authService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
                String token = authService.generateToken(user);



                response.setHeader(
                        "Set-Cookie",
                        "authToken=" + token +
                                "; HttpOnly; Secure; SameSite=None; Path=/; Max-Age=3600"
                );



                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("message", "Login successful");
                responseBody.put("role", user.getRole().name());
                responseBody.put("username", user.getUsername());

                return ResponseEntity.ok(responseBody);

            }
            catch (RuntimeException e)
            {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
            }
        }


        @PostMapping("/logout")
        public ResponseEntity<Map<String, String>> logout(HttpServletRequest request, HttpServletResponse response) {
            try {
                User user=(User) request.getAttribute("authenticatedUser");
                if (user != null) {
                    authService.logout(user);
                }


                response.setHeader(
                        "Set-Cookie",
                        "authToken=; HttpOnly; Secure; SameSite=None; Path=/; Max-Age=0"
                );


                Map<String, String> responseBody = new HashMap<>();
                responseBody.put("message", "Logout successful");
                return ResponseEntity.ok(responseBody);
            } catch (RuntimeException e) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "Logout failed");
                return ResponseEntity.status(500).body(errorResponse);
            }

    }
    }





package com.example.Sale_Savvy.Services;

import com.example.Sale_Savvy.Entities.JWTToken;
import com.example.Sale_Savvy.Entities.User;
import com.example.Sale_Savvy.Repository.JWTTokenRepository;
import com.example.Sale_Savvy.Repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JWTTokenRepository jwtTokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Key SIGNING_KEY;

    @Autowired
    public AuthService(UserRepository userRepository,
                       JWTTokenRepository jwtTokenRepository,
                       @Value("${jwt.secret}") String jwtSecret) {

        this.userRepository = userRepository;
        this.jwtTokenRepository = jwtTokenRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();

        this.SIGNING_KEY = Keys.hmacShaKeyFor(
                jwtSecret.getBytes(StandardCharsets.UTF_8)
        );
    }

    public User authenticate(String username, String password) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        return user;
    }

    public String generateToken(User user) {

        String token;
        LocalDateTime now = LocalDateTime.now();

        JWTToken existingToken =
                jwtTokenRepository.findByUserId(user.getUserId());

        if (existingToken != null &&
                now.isBefore(existingToken.getExpiresAt())) {

            token = existingToken.getToken();

        } else {

            token = generateNewToken(user);

            if (existingToken != null) {
                jwtTokenRepository.delete(existingToken);
            }

            saveToken(user, token);
        }

        return token;
    }

    private String generateNewToken(User user) {

//        here we are  generating the token with username and role
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + 3600000)
                )
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS512)
                .compact();
    }

    public void saveToken(User user, String token) {

        JWTToken jwtToken = new JWTToken(
                user,
                token,
                LocalDateTime.now().plusHours(1)
        );

        jwtTokenRepository.save(jwtToken);
    }

    public void logout(User user) {
        jwtTokenRepository.deleteByUserId(user.getUserId());
    }

    public boolean validateToken(String token) {

        try {

            Jwts.parserBuilder()
                    .setSigningKey(SIGNING_KEY)
                    .build()
                    .parseClaimsJws(token);

            Optional<JWTToken> jwtToken =
                    jwtTokenRepository.findByToken(token);

            return jwtToken.isPresent() &&
                    jwtToken.get()
                            .getExpiresAt()
                            .isAfter(LocalDateTime.now());

        } catch (Exception e) {

            System.out.println("Token validation failed: "
                    + e.getMessage());

            return false;
        }
    }

    public String extractUsername(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(SIGNING_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
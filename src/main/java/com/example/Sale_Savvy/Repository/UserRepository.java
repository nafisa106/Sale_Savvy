package com.example.Sale_Savvy.Repository;

import com.example.Sale_Savvy.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

}


//stateless -> independent to the previous request, means do not depend on previous login
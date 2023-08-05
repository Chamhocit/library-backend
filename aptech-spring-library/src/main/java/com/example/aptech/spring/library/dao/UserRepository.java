package com.example.aptech.spring.library.dao;

import com.example.aptech.spring.library.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    boolean existsUserByEmail(String email);
    boolean existsUserByPhone(String phone);
}

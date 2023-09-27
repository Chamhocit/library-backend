package com.example.aptech.spring.library.dao;

import com.example.aptech.spring.library.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    boolean existsUserByEmail(String email);
    boolean existsUserByPhone(String phone);
}

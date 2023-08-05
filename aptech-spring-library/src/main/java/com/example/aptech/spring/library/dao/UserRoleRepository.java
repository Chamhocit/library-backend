package com.example.aptech.spring.library.dao;

import com.example.aptech.spring.library.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}

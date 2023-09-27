package com.example.aptech.spring.library.dao;

import com.example.aptech.spring.library.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Query("select r from Role r inner join UserRole ur on ur.role.id = r.id inner join User u on ur.user.id = u.id where u.id=:id")
    List<Role> findAllRoleByUser(Integer id);
}

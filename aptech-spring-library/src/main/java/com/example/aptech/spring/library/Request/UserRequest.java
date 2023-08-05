package com.example.aptech.spring.library.Request;
import com.example.aptech.spring.library.Service.UniqueEmail;
import com.example.aptech.spring.library.entity.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @NotBlank(message = "Username shouldn't be null")
    private String name;
    @Pattern(regexp = "^\\d{10}$", message = "Invalid mobile number entered")
    private String phone;
    @Email(message = "Invalid email address")
    @UniqueEmail(message = "Email is already")
    private String email;
    @NotBlank(message = "Password shouldn't be null")
    private String password;
    @NotEmpty(message = "User Roles shouldn't be null ")
    private Set<Role> roles;

}

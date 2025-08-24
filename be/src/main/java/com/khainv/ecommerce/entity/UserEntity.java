package com.khainv.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class UserEntity extends  AbstractEntity<Long>{
    @NotEmpty(message = "Email is required.")
    @Email(message = "Valid email is required.")
    private String email;

    @NotEmpty(message = "Password is required.")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

}

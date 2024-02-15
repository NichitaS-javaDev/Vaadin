package org.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(min = 3, max = 15)
    private String name;
    @Email
    @Column(unique = true)
    private String email;
    @NotBlank
    @Size(min = 3, max = 10)
    @Column(unique = true)
    private String username;
    @NotBlank
    @Size(min = 8, max = 25)
    private String password;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private UserRole role;
}

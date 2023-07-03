package com.mammadli.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mammadli.ecommerce.enums.Gender;
import com.mammadli.ecommerce.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "[user]")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name",nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;


    @Column(name = "user_name",length = 50, unique = true, nullable = false)
    private String userName;

    @Column(length = 254,unique = true,nullable = false)
    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "password_hash", length = 60, nullable = false)
    private String password;

    private Integer age;

    @JsonIgnore
    @OneToMany(mappedBy = "user",
            fetch = FetchType.LAZY)
    private List<Order> orders;
}

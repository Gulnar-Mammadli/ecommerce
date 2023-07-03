package com.mammadli.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mammadli.ecommerce.enums.Role;
import com.mammadli.ecommerce.model.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String userName;

    private String email;

    private String role;

    private String password;

    private String gender;
    private Integer age;

    private List<Order> orders;
}

package com.mammadli.ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpDto {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String userName;

    @Size(min = 6, max = 20)
    @NotNull
    private String password;

    private String role;

    private String token;

    @NotNull
    @Email
    @Size(max = 50)
    private String email;

    private String gender;
    private Integer age;
}

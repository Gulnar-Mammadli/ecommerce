package com.mammadli.ecommerce.service;

import com.mammadli.ecommerce.dto.SignUpDto;
import com.mammadli.ecommerce.enums.Gender;
import com.mammadli.ecommerce.enums.Role;
import com.mammadli.ecommerce.jwt.JwtService;
import com.mammadli.ecommerce.model.User;
import com.mammadli.ecommerce.reposirory.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final JwtService jwtService;

    public SignUpDto signUp(SignUpDto signUpDto) {

        User registeredUser = User.builder()
                .firstName(signUpDto.getFirstName())
                .lastName(signUpDto.getLastName())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .age(signUpDto.getAge())
                .gender(Gender.valueOf(signUpDto.getGender()))
                .email(signUpDto.getEmail())
                .userName(signUpDto.getUserName())
                .role(Role.USER)
                .build();
        userRepository.save(registeredUser);
        signUpDto.setToken(jwtService.generateToken(signUpDto.getUserName()));
        signUpDto.setRole(Role.USER.toString());
        return signUpDto;
    }
}

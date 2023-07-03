package com.mammadli.ecommerce.controller;

import com.mammadli.ecommerce.dto.LoginDto;
import com.mammadli.ecommerce.dto.SignUpDto;
import com.mammadli.ecommerce.jwt.JwtService;
import com.mammadli.ecommerce.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;


    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/signUp")
    public ResponseEntity<SignUpDto> registerUser(@Valid @RequestBody SignUpDto signUpDto){
        return ResponseEntity.ok().body(userService.signUp(signUpDto));
    }

    @PostMapping("/login")
    public String authenticateAndGetToken(@RequestBody LoginDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUserName(), loginDto.getPassword()));
        log.info("authentication.isAuthenticated()  {} ", authentication);
        if (authentication.isAuthenticated()) {
            log.info("jwtService.generateToken(authRequest.getName())  {} ", jwtService.generateToken(loginDto.getUserName()));
            return jwtService.generateToken(loginDto.getUserName());
        } else {
            throw new UsernameNotFoundException("The user cannot be authenticated");
        }
    }
}

package com.mammadli.ecommerce.controller;

import com.mammadli.ecommerce.dto.SignUpDto;
import com.mammadli.ecommerce.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity<SignUpDto> registerUser(@Valid @RequestBody SignUpDto signUpDto){
        return ResponseEntity.ok().body(userService.signUp(signUpDto));
    }

}

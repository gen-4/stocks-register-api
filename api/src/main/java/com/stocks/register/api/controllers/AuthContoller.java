package com.stocks.register.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stocks.register.api.dtos.auth.AuthenticationResponseDto;
import com.stocks.register.api.dtos.auth.RegisterRequestDto;
import com.stocks.register.api.exceptions.NotFoundException;
import com.stocks.register.api.exceptions.WrongParametersException;
import com.stocks.register.api.services.AuthenticationService;
import com.stocks.register.api.dtos.auth.AuthenticationRequestDto;

import lombok.RequiredArgsConstructor;



@RestController
@CrossOrigin
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthContoller {

    @Autowired
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDto> register(
        @RequestBody RegisterRequestDto request
    ) throws NotFoundException, WrongParametersException {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(
        @RequestBody AuthenticationRequestDto request
    ) throws WrongParametersException, NotFoundException {
        return ResponseEntity.ok(authenticationService.login(request));
    }

}

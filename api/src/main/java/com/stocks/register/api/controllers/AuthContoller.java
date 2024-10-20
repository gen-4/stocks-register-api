package com.stocks.register.api.controllers;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stocks.register.api.dtos.auth.AuthenticationResponseDto;
import com.stocks.register.api.dtos.auth.RegisterRequestDto;
import com.stocks.register.api.dtos.user.RoleDto;
import com.stocks.register.api.dtos.user.UserDto;
import com.stocks.register.api.exceptions.NotFoundException;
import com.stocks.register.api.exceptions.UnauthorizedException;
import com.stocks.register.api.exceptions.WrongParametersException;
import com.stocks.register.api.models.user.User;
import com.stocks.register.api.services.AuthenticationService;
import com.stocks.register.api.configuration.JwtInfo;
import com.stocks.register.api.configuration.JwtService;
import com.stocks.register.api.dtos.auth.AuthenticationRequestDto;

import lombok.RequiredArgsConstructor;



@RestController
@CrossOrigin
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthContoller {

    private final AuthenticationService authenticationService;

    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDto> register(
        @RequestBody RegisterRequestDto request
    ) throws NotFoundException, WrongParametersException {
        User user = authenticationService.register(request.getEmail(), request.getUsername(), request.getPassword());
        
        return ResponseEntity.ok(AuthenticationResponseDto.builder()
            .user(UserDto.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .registerDate(user.getRegisterDate())
                .lastLogin(user.getLastLogin())
                .roles(user.getRoles().stream()
                    .map(role -> RoleDto.builder()
                        .id(role.getId())
                        .role(role.getRole().name())
                        .build()
                    )
                    .collect(Collectors.toList())
                )
                .build()
            )
            .token(generateServiceToken(user))
            .build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(
        @RequestBody AuthenticationRequestDto request
    ) throws WrongParametersException, NotFoundException, UnauthorizedException {
        User user = authenticationService.login(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(AuthenticationResponseDto.builder()
            .user(UserDto.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .registerDate(user.getRegisterDate())
                .lastLogin(user.getLastLogin())
                .roles(user.getRoles().stream()
                    .map(role -> RoleDto.builder()
                        .id(role.getId())
                        .role(role.getRole().name())
                        .build()
                    )
                    .collect(Collectors.toList())
                )
                .build()
            )
            .token(generateServiceToken(user))
            .build()
        );
    }

    @PostMapping("/login-with-token")
    public ResponseEntity<AuthenticationResponseDto> loginWithToken(@RequestAttribute long userId) 
        throws NotFoundException, UnauthorizedException {
            User user = authenticationService.loginWithToken(userId);

            return ResponseEntity.ok(AuthenticationResponseDto.builder()
            .user(UserDto.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .registerDate(user.getRegisterDate())
                .lastLogin(user.getLastLogin())
                .roles(user.getRoles().stream()
                    .map(role -> RoleDto.builder()
                        .id(role.getId())
                        .role(role.getRole().name())
                        .build()
                    )
                    .collect(Collectors.toList())
                )
                .build()
            )
            .build()
        );
    }

    private String generateServiceToken(User user) {

		JwtInfo jwtInfo = JwtInfo.builder()
            .userId(user.getId())
            .username(user.getUsername())
            .roles(user.getRoles().stream()
                .map( role ->
                    role.getRole()
                )
                .collect(Collectors.toList())
            )
            .build();

		return jwtService.generateToken(jwtInfo);

	}

}

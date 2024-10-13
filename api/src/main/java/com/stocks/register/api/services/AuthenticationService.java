package com.stocks.register.api.services;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.List;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.stocks.register.api.exceptions.NotFoundException;
import com.stocks.register.api.exceptions.WrongParametersException;
import com.stocks.register.api.configuration.JwtService;
import com.stocks.register.api.dtos.auth.AuthenticationRequestDto;
import com.stocks.register.api.dtos.auth.AuthenticationResponseDto;
import com.stocks.register.api.dtos.auth.RegisterRequestDto;
import com.stocks.register.api.models.user.Role;
import com.stocks.register.api.models.user.RoleOptions;
import com.stocks.register.api.models.user.User;
import com.stocks.register.api.repositories.user.RoleRepository;
import com.stocks.register.api.repositories.user.UserRepository;

import lombok.RequiredArgsConstructor;




@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponseDto register(RegisterRequestDto request) 
        throws NotFoundException, WrongParametersException {
        Optional<Role> userRole = roleRepository.findByRole(RoleOptions.USER);
        if (!userRole.isPresent()) {
            throw new NotFoundException("Role", RoleOptions.USER.name());
        }

        User user = User.builder()
            .username(request.getUsername())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .registerDate(new Timestamp(System.currentTimeMillis()))
            .roles(List.of(userRole.get()))
            .build();
        
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new WrongParametersException("User");
        }

        String token = jwtService.generateToken(user);
        
        return AuthenticationResponseDto.builder()
            .token(token)
            .build();
    }

    public AuthenticationResponseDto login(AuthenticationRequestDto request) throws NotFoundException {
        String email = request.getEmail();
        Optional<User> optionalUser;
        User user;
        String token;

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                email,
                request.getPassword()
            )
        );

        optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            throw new NotFoundException("User", email);
        }
        
        user = optionalUser.get();
        user.setLastLogin(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);

        token = jwtService.generateToken(user);


        return AuthenticationResponseDto.builder()
            .token(token)
            .build();
    }

}

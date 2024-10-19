package com.stocks.register.api.services;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.List;

import org.springframework.data.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.stocks.register.api.exceptions.NotFoundException;
import com.stocks.register.api.exceptions.WrongParametersException;
import com.stocks.register.api.configuration.JwtService;
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

    public Pair<User, String> register(String email, String username, String password) 
        throws NotFoundException, WrongParametersException {
        User user;
        Optional<Role> userRole = roleRepository.findByRole(RoleOptions.USER);
        if (!userRole.isPresent()) {
            throw new NotFoundException("Role", RoleOptions.USER.name());
        }

        try {
            user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .registerDate(new Timestamp(System.currentTimeMillis()))
                .roles(List.of(userRole.get()))
                .build();
        
            userRepository.save(user);
            
        } catch (DataIntegrityViolationException e) {
            throw new WrongParametersException("User Register Request");
        } catch (IllegalArgumentException e) {
            throw new WrongParametersException("User Register Request");
        } catch (NullPointerException e) {
            throw new WrongParametersException("User Register Request");
        }

        String token = jwtService.generateToken(user);
        
        return Pair.of(user, token);
    }

    public Pair<User, String> login(String email, String password) 
        throws NotFoundException, WrongParametersException {
        Optional<User> optionalUser;
        User user;
        String token;

        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    email,
                    password
                )
            );
        } catch (BadCredentialsException e) {
            throw new WrongParametersException("User Authentication Request");
        }

        optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            throw new NotFoundException("User", email);
        }
        
        user = optionalUser.get();
        user.setLastLogin(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);

        token = jwtService.generateToken(user);


        return Pair.of(user, token);
    }

    public User loginWithToken(long userId)
        throws NotFoundException {
            Optional<User> optionalUser = userRepository.findById(userId);
            if (!optionalUser.isPresent()) {
                throw new NotFoundException("User", "id");
            }

            return optionalUser.get();
        }

}

package com.stocks.register.api.services;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.stocks.register.api.exceptions.DuplicatedEntityException;
import com.stocks.register.api.exceptions.NotFoundException;
import com.stocks.register.api.exceptions.UnauthorizedException;
import com.stocks.register.api.exceptions.WrongParametersException;
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

    public User register(String email, String username, String password) 
        throws NotFoundException, WrongParametersException, DuplicatedEntityException {
        User user;
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Optional<Role> userRole = roleRepository.findByRole(RoleOptions.USER);
        if (!userRole.isPresent()) {
            throw new NotFoundException("Role", RoleOptions.USER.name());
        }

        try {
            user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .registerDate(now)
                .lastLogin(now)
                .isBanned(false)
                .isEnabled(true)
                .roles(List.of(userRole.get()))
                .build();
        
            userRepository.save(user);
            
        } catch (DataIntegrityViolationException e) {
            throw new DuplicatedEntityException("User Register Request");
        } catch (IllegalArgumentException e) {
            throw new WrongParametersException("User Register Request");
        } catch (NullPointerException e) {
            throw new WrongParametersException("User Register Request");
        }
        
        return user;
    }

    public User login(String email, String password) 
        throws NotFoundException, WrongParametersException, UnauthorizedException {
        Optional<User> optionalUser;
        User user;

        optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            throw new NotFoundException("User", email);
        }
        
        user = optionalUser.get();

        if (!passwordEncoder.matches(password, user.getPassword()) || user.isBanned()) {
            throw new UnauthorizedException(email);
        }

        user.setLastLogin(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);

        return user;
    }

    public User loginWithToken(long userId)
        throws NotFoundException, UnauthorizedException {

        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new NotFoundException("User", "id");
        }

        User user = optionalUser.get();
        
        if (user.isBanned()) {
            throw new UnauthorizedException(user.getEmail());
        }

        user.setLastLogin(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);

        return user;
    }    

}

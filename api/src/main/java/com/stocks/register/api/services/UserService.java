package com.stocks.register.api.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.stocks.register.api.exceptions.NotFoundException;
import com.stocks.register.api.models.user.User;
import com.stocks.register.api.repositories.UserRepository;

import lombok.RequiredArgsConstructor;





@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserByEmail(String email) throws NotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (!user.isPresent()) {
            throw new NotFoundException("User", email);
        }

        return user.get();
    }

}

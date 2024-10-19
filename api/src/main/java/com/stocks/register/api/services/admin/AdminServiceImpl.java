package com.stocks.register.api.services.admin;

import java.util.Optional;
import java.util.List;

import org.springframework.stereotype.Service;

import com.stocks.register.api.exceptions.NotFoundException;
import com.stocks.register.api.exceptions.TryingToBanAdminException;
import com.stocks.register.api.models.user.RoleOptions;
import com.stocks.register.api.models.user.User;
import com.stocks.register.api.repositories.user.UserRepository;

import lombok.RequiredArgsConstructor;





@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public String banUser(long userId) throws NotFoundException, TryingToBanAdminException {
        User user;
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new NotFoundException("User", "Id[" + Long.valueOf(userId).toString() + "]");
        }

        user = optionalUser.get();
        if (user.hasRole(RoleOptions.ADMIN)) {
            throw new TryingToBanAdminException(userId);
        }

        user.setBanned(true);
        userRepository.save(user);

        return "User[" + userId + "] banned";
    }

}

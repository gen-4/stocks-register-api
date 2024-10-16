package com.stocks.register.api.services.admin;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.stocks.register.api.exceptions.NotFoundException;
import com.stocks.register.api.exceptions.TryingToBanAdminException;
import com.stocks.register.api.models.user.RoleOptions;
import com.stocks.register.api.models.user.User;
import com.stocks.register.api.repositories.user.UserRepository;
import com.stocks.register.api.dtos.admin.BanDto;
import com.stocks.register.api.dtos.user.RoleDto;
import com.stocks.register.api.dtos.user.UserDto;

import lombok.RequiredArgsConstructor;





@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    @Override
    public UserDto getUserByEmail(String email) throws NotFoundException {
        User user;
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            throw new NotFoundException("User", email);
        }

        user = optionalUser.get();
        
        return UserDto.builder()
            .id(user.getId())
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
            .build();
    }

    @Override
    public List<UserDto> getAll() {
        List<User> users = userRepository.findAll();
        
        return users.stream()
            .map(user -> UserDto.builder()
                .id(user.getId())
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
            .collect(Collectors.toList());
    }

    @Override
    public BanDto banUser(long userId) throws NotFoundException, TryingToBanAdminException {
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

        return BanDto.builder()
            .id(userId)
            .message("User[" + userId + "] banned")
            .build();
    }

}

package com.stocks.register.api.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.stocks.register.api.dtos.admin.BanDto;
import com.stocks.register.api.dtos.user.RoleDto;
import com.stocks.register.api.dtos.user.UserDto;
import com.stocks.register.api.exceptions.NotFoundException;
import com.stocks.register.api.exceptions.TryingToBanAdminException;
import com.stocks.register.api.services.admin.AdminService;

import lombok.RequiredArgsConstructor;






@RestController
@CrossOrigin
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok(adminService.getAll().stream()
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
            .collect(Collectors.toList())
        );
    }

    @PostMapping("/ban/{id}")
    public ResponseEntity<BanDto> banUser(@PathVariable long userId) 
        throws NotFoundException, TryingToBanAdminException {
        return ResponseEntity.ok(
            BanDto.builder()
            .id(userId)
            .message(adminService.banUser(userId))
            .build()
        );
    }
    

}

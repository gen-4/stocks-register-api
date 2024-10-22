package com.stocks.register.api.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.stocks.register.api.dtos.admin.ActionDto;
import com.stocks.register.api.dtos.admin.BanRequestDto;
import com.stocks.register.api.dtos.admin.ManageRoleRequestDto;
import com.stocks.register.api.dtos.stock.StockRequestResponseDto;
import com.stocks.register.api.dtos.user.RoleDto;
import com.stocks.register.api.dtos.user.UserDto;
import com.stocks.register.api.exceptions.ActionFailedException;
import com.stocks.register.api.exceptions.NotFoundException;
import com.stocks.register.api.exceptions.TryingToManageAdminException;
import com.stocks.register.api.services.admin.AdminService;

import lombok.RequiredArgsConstructor;








@RestController
@CrossOrigin
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

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
                .isBanned(user.isBanned())
                .isEnabled(user.isEnabled())
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

    @GetMapping("/roles")
    public ResponseEntity<List<RoleDto>> getRoles() {
        return ResponseEntity.ok(adminService.getRoles().stream()
            .map( role ->
                RoleDto.builder()
                .id(role.getId())
                .role(role.getRole().name())
                .build()
            )
            .collect(Collectors.toList())
        );
    }
    

    @PostMapping("/user/{userId}/ban")
    public ResponseEntity<ActionDto> banUser(
        @PathVariable long userId,
        @RequestBody BanRequestDto request
    ) throws NotFoundException, TryingToManageAdminException {
        return ResponseEntity.ok(
            ActionDto.builder()
            .id(userId)
            .message(adminService.banUser(userId, request.isBan()))
            .build()
        );
    }

    @DeleteMapping("/user/{userId}/role")
    public ResponseEntity<ActionDto> removeRole(
        @PathVariable long userId,
        @RequestBody ManageRoleRequestDto request
    ) throws NotFoundException, TryingToManageAdminException, ActionFailedException {
        return ResponseEntity.ok(
            ActionDto.builder()
            .id(userId)
            .message(adminService.manageRole(
                userId, 
                request.getRoleId(), 
                AdminService.REMOVE_ROLE_ACTION
            ))
            .build()
        );
    }  
    
    @PutMapping("/user/{userId}/role")
    public ResponseEntity<ActionDto> addRole(
        @PathVariable long userId,
        @RequestBody ManageRoleRequestDto request
    ) throws NotFoundException, TryingToManageAdminException, ActionFailedException {
        return ResponseEntity.ok(
            ActionDto.builder()
            .id(userId)
            .message(adminService.manageRole(
                userId, 
                request.getRoleId(), 
                AdminService.ADD_ROLE_ACTION
            ))
            .build()
        );
    }  

    @GetMapping("/stocks/requests")
    public ResponseEntity<List<StockRequestResponseDto>> getStockRequests() {
        return ResponseEntity.ok(
            adminService.getStockRequests().stream().map( request ->
                StockRequestResponseDto.builder()
                .id(request.getId())
                .name(request.getName())
                .status(request.getStatus())
                .registerDate(request.getRegisterDate())
                .aprovalDate(request.getAprovalDate())
                .build()
            )
            .collect(Collectors.toList())
        );
    }
    

}

package com.stocks.register.api.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.stocks.register.api.dtos.admin.BanDto;
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
        return ResponseEntity.ok(adminService.getAll());
    }

    @PostMapping("/ban/{id}")
    public ResponseEntity<BanDto> banUser(@PathVariable long userId) 
        throws NotFoundException, TryingToBanAdminException {
        return ResponseEntity.ok(adminService.banUser(userId));
    }
    

}

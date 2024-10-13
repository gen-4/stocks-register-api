package com.stocks.register.api.dtos.user;

import java.util.List;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class UserDto {

    private long id;
    private String username;
    private String email;
    private Timestamp registerDate;
    private Timestamp lastLogin;
    private List<RoleDto> roles;
    
}

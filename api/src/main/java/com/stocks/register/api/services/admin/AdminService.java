package com.stocks.register.api.services.admin;

import java.util.List;

import com.stocks.register.api.dtos.user.UserDto;
import com.stocks.register.api.exceptions.NotFoundException;



public interface AdminService {
    
    public UserDto getUserByEmail(String email) throws NotFoundException;

    public List<UserDto> getAll();
    
}

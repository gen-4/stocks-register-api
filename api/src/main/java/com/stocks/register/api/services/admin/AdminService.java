package com.stocks.register.api.services.admin;

import java.util.List;

import com.stocks.register.api.dtos.admin.BanDto;
import com.stocks.register.api.dtos.user.UserDto;
import com.stocks.register.api.exceptions.NotFoundException;
import com.stocks.register.api.exceptions.TryingToBanAdminException;



public interface AdminService {
    
    public UserDto getUserByEmail(String email) throws NotFoundException;

    public List<UserDto> getAll();

    public BanDto banUser(long userId) throws NotFoundException, TryingToBanAdminException;
    
}

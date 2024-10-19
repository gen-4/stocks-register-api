package com.stocks.register.api.services.admin;

import java.util.List;
                                                   
import com.stocks.register.api.exceptions.NotFoundException;
import com.stocks.register.api.exceptions.TryingToBanAdminException;
import com.stocks.register.api.models.user.User;



public interface AdminService {
    
    public List<User> getAll();

    public String banUser(long userId) throws NotFoundException, TryingToBanAdminException;
    
}

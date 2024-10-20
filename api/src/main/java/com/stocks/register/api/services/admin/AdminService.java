package com.stocks.register.api.services.admin;

import java.util.List;

import com.stocks.register.api.exceptions.ActionFailedException;
import com.stocks.register.api.exceptions.NotFoundException;
import com.stocks.register.api.exceptions.TryingToManageAdminException;
import com.stocks.register.api.models.user.Role;
import com.stocks.register.api.models.user.User;



public interface AdminService {

    public final static String REMOVE_ROLE_ACTION = "REMOVE_ROLE";
    public final static String ADD_ROLE_ACTION = "ADD_ROLE";
    
    public List<User> getAll();

    public List<Role> getRoles();

    public String banUser(long userId, boolean ban) throws NotFoundException, TryingToManageAdminException;

    public String manageRole(long userId, long roleId, String action) 
        throws NotFoundException, TryingToManageAdminException, ActionFailedException;
    
}

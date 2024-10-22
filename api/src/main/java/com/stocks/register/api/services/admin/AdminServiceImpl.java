package com.stocks.register.api.services.admin;

import java.util.Optional;
import java.util.List;

import org.springframework.stereotype.Service;

import com.stocks.register.api.exceptions.ActionFailedException;
import com.stocks.register.api.exceptions.NotFoundException;
import com.stocks.register.api.exceptions.TryingToManageAdminException;
import com.stocks.register.api.models.stock.StockRequest;
import com.stocks.register.api.models.user.Role;
import com.stocks.register.api.models.user.RoleOptions;
import com.stocks.register.api.models.user.User;
import com.stocks.register.api.repositories.stocks.StockRequestRepository;
import com.stocks.register.api.repositories.user.RoleRepository;
import com.stocks.register.api.repositories.user.UserRepository;

import lombok.RequiredArgsConstructor;





@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StockRequestRepository stockRequestRepository;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public String banUser(long userId, boolean ban) throws NotFoundException, TryingToManageAdminException {
        User user;
        String action;
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new NotFoundException("User", "Id[" + Long.valueOf(userId).toString() + "]");
        }

        user = optionalUser.get();
        if (user.hasRole(RoleOptions.ADMIN)) {
            throw new TryingToManageAdminException(userId);
        }

        user.setBanned(ban);
        userRepository.save(user);
        action = (ban) ? "banned" : "unbanned";

        return "User[" + user.getUsername() + "] " + action;
    }

    public String manageRole(long userId, long roleId, String action)
        throws NotFoundException, TryingToManageAdminException, ActionFailedException {
            
        User user;
        List<Role> userRoles;
        Optional<Role> role;
        String actionStr;
        boolean succeeded = false;
        Optional<User> optionalUser = userRepository.findById(userId);
        
        if (!optionalUser.isPresent()) {
            throw new NotFoundException("User", "Id[" + Long.valueOf(userId).toString() + "]");
        }

        user = optionalUser.get();
        if (user.hasRole(RoleOptions.ADMIN)) {
            throw new TryingToManageAdminException(userId);
        }

        role = roleRepository.findById(roleId);
        if (!role.isPresent()) {
            throw new NotFoundException("Role", "Id[" + Long.valueOf(roleId).toString() + "]");
        }

        userRoles = user.getRoles();
        if (action == AdminService.ADD_ROLE_ACTION) {
            succeeded = userRoles.add(role.get());
            actionStr = "added";

        } else {
            succeeded = userRoles.remove(role.get());
            actionStr = "removed";
        }

        if (!succeeded) {
            throw new ActionFailedException("Role[" + role.get().getRole().name()+ "] management on User[" + user.getUsername() + "] failed");
        }

        user.setRoles(userRoles);
        userRepository.save(user);

        return "User[" + user.getUsername() + "] role " + role.get().getRole().name() + " " + actionStr;
    }

    @Override
    public List<StockRequest> getStockRequests() {
        return stockRequestRepository.findAll();
    }

}

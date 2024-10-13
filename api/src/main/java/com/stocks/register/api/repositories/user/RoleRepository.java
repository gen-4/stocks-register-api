package com.stocks.register.api.repositories.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stocks.register.api.models.user.Role;
import com.stocks.register.api.models.user.RoleOptions;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    public Optional<Role> findByRole(RoleOptions role);
    
}

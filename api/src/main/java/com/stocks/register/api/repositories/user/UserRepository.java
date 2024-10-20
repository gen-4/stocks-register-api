package com.stocks.register.api.repositories.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stocks.register.api.models.user.User;





public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByEmail(String email);

}

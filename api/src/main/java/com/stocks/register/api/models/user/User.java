package com.stocks.register.api.models.user;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stocks_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Timestamp registerDate;

    private Timestamp lastLogin;

    @ColumnDefault("false")
    @Column(nullable = false)
    private boolean isBanned;

    @ColumnDefault("true")
    @Column(nullable = false)
    private boolean isEnabled;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;

	public boolean isAccountNonExpired() {
		return true;
	}

	public boolean isAccountNonLocked() {
		return !isBanned;
	}

	public boolean isCredentialsNonExpired() {
		return true;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

    public boolean hasRole(RoleOptions roleOptions) {
        for (Role role: roles) {
            if (role.getRole().equals(roleOptions)) {
                return true;
            }
        }

        return false;
    } 
    
}

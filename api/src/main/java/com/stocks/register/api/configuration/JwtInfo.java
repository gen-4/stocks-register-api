package com.stocks.register.api.configuration;

import java.util.List;

import com.stocks.register.api.models.user.RoleOptions;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtInfo {
	private Long userId;

	private String username;

	private List<RoleOptions> roles;
}

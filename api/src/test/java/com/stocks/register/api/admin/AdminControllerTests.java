package com.stocks.register.api.admin;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.stocks.register.api.controllers.AdminController;
import com.stocks.register.api.dtos.admin.ActionDto;
import com.stocks.register.api.dtos.admin.BanRequestDto;
import com.stocks.register.api.dtos.user.UserDto;
import com.stocks.register.api.exceptions.NotFoundException;
import com.stocks.register.api.exceptions.TryingToManageAdminException;
import com.stocks.register.api.services.admin.AdminService;

import lombok.RequiredArgsConstructor;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@RequiredArgsConstructor
class AdminControllerTests {

    @Autowired
	private Environment env;

	@Autowired
	private AdminController adminDashboardController;

	@MockBean
    private AdminService adminService;

	@BeforeAll
	void checkTestEnv() {
		assertTrue(env.getProperty("application.env").equals("test"));
	}

    @Test
    public void getAllUsers() {
		when(adminService.getAll())
			.thenReturn(List.of());
        ResponseEntity<List<UserDto>> users = adminDashboardController.getUsers();

        assertTrue(users.getStatusCode().equals(HttpStatus.OK));
    }

	@Test
    public void banUser() throws NotFoundException, TryingToManageAdminException {
		long userId = 0;
		when(adminService.banUser(userId, true))
			.thenReturn("User banned");
        ResponseEntity<ActionDto> users = adminDashboardController.banUser(
			userId, 
			BanRequestDto.builder()
				.ban(true)
				.build()
		);

        assertTrue(users.getStatusCode().equals(HttpStatus.OK));
    }

	@Test
    public void banUserThrowsNotFoundException() throws NotFoundException, TryingToManageAdminException {
		long userId = 0;
		when(adminService.banUser(userId, true))
			.thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> adminDashboardController.banUser(
			userId,
			BanRequestDto.builder()
				.ban(true)
				.build()
		));
    }

}

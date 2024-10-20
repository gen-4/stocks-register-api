package com.stocks.register.api.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Timestamp;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import com.stocks.register.api.exceptions.NotFoundException;
import com.stocks.register.api.exceptions.TryingToManageAdminException;
import com.stocks.register.api.models.user.Role;
import com.stocks.register.api.models.user.RoleOptions;
import com.stocks.register.api.models.user.User;
import com.stocks.register.api.repositories.user.RoleRepository;
import com.stocks.register.api.repositories.user.UserRepository;
import com.stocks.register.api.services.admin.AdminService;

import lombok.RequiredArgsConstructor;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@RequiredArgsConstructor
class AdminServiceTests {

    @Autowired
	private Environment env;

    @Autowired
    private AdminService adminService;

    @Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	private static final long UNEXISTENT_ID = -1;

	private static final String adminEmail = "admin@example.com";
	private static final String adminPassword = "1234admin";
	private static final String adminUsername = "admin";

	private static final String userEmail = "user@example.com";
	private static final String userPassword = "1234user";
	private static final String userUsername = "user";

	private User userUser;
	private User adminUser;

	@BeforeAll
	void checkTestEnv() {
		assertTrue(env.getProperty("application.env").equals("test"));
	}

	@BeforeEach
	public void setUp() {
		Role adminRole = roleRepository.save(Role.builder()
			.role(RoleOptions.ADMIN)
			.build()
		);
		Role userRole = roleRepository.save(Role.builder()
			.role(RoleOptions.USER)
			.build()
		);

		adminUser = User.builder()
			.email(adminEmail)
			.password(adminPassword)
			.username(adminUsername)
			.registerDate(new Timestamp(System.currentTimeMillis()))
			.roles(List.of(userRole, adminRole))
			.build();

		userUser = User.builder()
			.email(userEmail)
			.password(userPassword)
			.username(userUsername)
			.registerDate(new Timestamp(System.currentTimeMillis()))
			.roles(List.of(userRole))
			.build();

		userRepository.saveAll(List.of(adminUser, userUser));
	}

	@AfterEach
	public void tearDown() {
		userRepository.deleteAll();
		roleRepository.deleteAll();
	}

    @Test
    public void getAllUsers() {
        List<User> users = adminService.getAll();

        assertFalse(users.isEmpty());
        assertEquals(2, users.size());
    }

    @Test
    public void getAllUsersEmptyUsers() {
        userRepository.deleteAll();
        List<User> users = adminService.getAll();

        assertTrue(users.isEmpty());
    }

	@Test
    public void banUser() throws NotFoundException, TryingToManageAdminException {
        adminService.banUser(userUser.getId(), true);

        assertTrue(userRepository.findById(userUser.getId()).get().isBanned());
    }

	@Test
    public void banUserThrowsNotFoundException() throws NotFoundException, TryingToManageAdminException {
        assertThrows(NotFoundException.class, () -> adminService.banUser(UNEXISTENT_ID, true));
    }

	@Test
    public void banUserThrowsTryingToBanAdminException() throws NotFoundException, TryingToManageAdminException {
        assertThrows(TryingToManageAdminException.class, () -> adminService.banUser(adminUser.getId(), true));
    }

}

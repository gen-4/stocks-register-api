package com.stocks.register.api.auth;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import com.stocks.register.api.dtos.auth.AuthenticationRequestDto;
import com.stocks.register.api.dtos.auth.RegisterRequestDto;
import com.stocks.register.api.exceptions.NotFoundException;
import com.stocks.register.api.exceptions.WrongParametersException;
import com.stocks.register.api.models.user.Role;
import com.stocks.register.api.models.user.RoleOptions;
import com.stocks.register.api.models.user.User;
import com.stocks.register.api.repositories.user.RoleRepository;
import com.stocks.register.api.repositories.user.UserRepository;
import com.stocks.register.api.services.AuthenticationService;

import lombok.RequiredArgsConstructor;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@RequiredArgsConstructor
class AuthUserTests {

	@Autowired
	private Environment env;

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	private static final String testUserEmail = "testupser@example.com";
	private static final String testUserPassword = "1234test";
	private static final String testUserUsername = "testuser";

	private long userUserId;
	private static final String userEmail = "user@example.com";
	private static final String userPassword = "1234user";
	private static final String userUsername = "user";

	private static final long UNEXISTENT_ID = -1;

	@BeforeAll
	void checkTestEnv() {
		assertTrue(env.getProperty("application.env").equals("test"));
	}

	@BeforeEach
	public void setUp() {
		Role userRole = roleRepository.save(Role.builder()
			.role(RoleOptions.USER)
			.build()
		);

		User userUser = User.builder()
			.email(userEmail)
			.password(userPassword)
			.username(userUsername)
			.registerDate(new Timestamp(System.currentTimeMillis()))
			.roles(List.of(userRole))
			.build();

		User createdUser = userRepository.save(userUser);
		userUserId = createdUser.getId();
	}

	@AfterEach
	public void tearDown() {
		userRepository.deleteAll();
		roleRepository.deleteAll();
	}

	@Test
	void registerUserDoesNotThrowAnyExceptions() throws NotFoundException, WrongParametersException {
		assertDoesNotThrow(() -> authenticationService.register(
			testUserEmail, 
			testUserUsername, 
			testUserPassword
		));
	}

	@Test
	void registerUserThrowsNotFoundException() throws NotFoundException, WrongParametersException {
		userRepository.deleteAll();
		roleRepository.deleteAll();

		assertThrows(NotFoundException.class, () -> authenticationService.register(
			testUserEmail, 
			testUserUsername, 
			testUserPassword
		));
	}

	@Test
	void registerUserThrowsWrongParametersException() throws NotFoundException, WrongParametersException {
		assertThrows(WrongParametersException.class, () -> authenticationService.register(
			userEmail, 
			userUsername, 
			userPassword
		));
	}

	@Test
	void userLoginDoesNotThrowAnyException() throws NotFoundException {
		assertDoesNotThrow(() -> authenticationService.login(
			userEmail,
			userPassword
		));
	}

	@Test
	void userLoginThrowsWrongParamertsExceptionWrongEmail() throws WrongParametersException {
		assertThrows(WrongParametersException.class, () ->authenticationService.login(
			testUserEmail,
			userPassword
		));
	}

	@Test
	void userLoginThrowsWrongParamertsExceptionWrongPassword() throws WrongParametersException {
		assertThrows(WrongParametersException.class, () ->authenticationService.login(
			userEmail,
			testUserPassword
		));
	}

	@Test
	void userReauthenticate() throws NotFoundException {
		assertDoesNotThrow(() ->authenticationService.loginWithToken(userUserId));
	}

	@Test
	void userReauthenticateThrowsNotFoundExceptionUnexistentUser() throws NotFoundException {
		assertThrows(NotFoundException.class, () ->authenticationService.loginWithToken(UNEXISTENT_ID));
	}

}

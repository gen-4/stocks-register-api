package com.stocks.register.api;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class ApiApplicationTests {

	@Autowired
	private Environment env;

	@BeforeAll
	void checkTestEnv() {
		assertTrue(env.getProperty("application.env").equals("test"));
	}

	@Test
	void contextLoads() {
	}

}

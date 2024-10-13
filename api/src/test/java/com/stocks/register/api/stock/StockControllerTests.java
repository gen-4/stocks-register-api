package com.stocks.register.api.stock;

import static org.junit.jupiter.api.Assertions.assertTrue;
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

import com.stocks.register.api.controllers.StockController;
import com.stocks.register.api.dtos.stock.StockDto;
import com.stocks.register.api.services.stock.StockService;

import lombok.RequiredArgsConstructor;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@RequiredArgsConstructor
public class StockControllerTests {

    @Autowired
	private Environment env;

    @Autowired
    private StockController stockController;
    
    @MockBean
    private StockService stockService;

    @BeforeAll
	void checkTestEnv() {
		assertTrue(env.getProperty("application.env").equals("test"));
	}

    @Test
    public void getAllStocks() {
		when(stockService.getAll())
			.thenReturn(List.of());
        ResponseEntity<List<StockDto>> stocks = stockController.getAll();

        assertTrue(stocks.getStatusCode().equals(HttpStatus.OK));
    }


}

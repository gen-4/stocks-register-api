package com.stocks.register.api.stock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

import com.stocks.register.api.dtos.stock.StockDto;
import com.stocks.register.api.models.stock.Stock;
import com.stocks.register.api.repositories.stocks.StockRepository;
import com.stocks.register.api.services.stock.StockService;

import lombok.RequiredArgsConstructor;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@RequiredArgsConstructor
public class StockServiceTests {

    @Autowired
	private Environment env;

    @Autowired
    private StockService stockService;

    @Autowired
    private StockRepository stockRepository;

    @BeforeAll
	void checkTestEnv() {
		assertTrue(env.getProperty("application.env").equals("test"));
	}

    @BeforeEach
	public void setUp() {
        Stock stock1 = Stock.builder()
            .name("IBM")
            .build();
        Stock stock2 = Stock.builder()
            .name("APPLE")
            .build();
        stockRepository.saveAll(List.of(stock1, stock2));
	}

    @AfterEach
	public void tearDown() {
        stockRepository.deleteAll();
	}

    @Test
    public void getAllStocks() {
        List<StockDto> stocks = stockService.getAll();

        assertFalse(stocks.isEmpty());
        assertEquals(2, stocks.size());
    }

    @Test
    public void getAllStocksEmptyStocks() {
        stockRepository.deleteAll();
        List<StockDto> stocks = stockService.getAll();

        assertTrue(stocks.isEmpty());
    }

}

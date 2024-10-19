package com.stocks.register.api.services.stock;

import java.util.List;

import org.springframework.stereotype.Service;

import com.stocks.register.api.models.stock.Stock;
import com.stocks.register.api.repositories.stocks.StockRepository;

import lombok.RequiredArgsConstructor;




@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    @Override
    public List<Stock> getAll() {
        return stockRepository.findAll();
    }

    @Override // TODO: This will store a request, it will not automatically save the stock
    public Stock requestStock(String name) {
        Stock stock = Stock.builder()
            .id(name)
            .name(name)
            .build();
        return stockRepository.save(stock);
    }



}

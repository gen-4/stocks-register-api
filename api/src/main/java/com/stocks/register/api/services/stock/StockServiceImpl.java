package com.stocks.register.api.services.stock;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.stocks.register.api.models.stock.RequestStatus;
import com.stocks.register.api.models.stock.Stock;
import com.stocks.register.api.models.stock.StockRequest;
import com.stocks.register.api.repositories.stocks.StockRepository;
import com.stocks.register.api.repositories.stocks.StockRequestRepository;

import lombok.RequiredArgsConstructor;




@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    private final StockRequestRepository stockRequestRepository;

    @Override
    public List<Stock> getAll() {
        return stockRepository.findAll();
    }

    @Override
    public StockRequest requestStock(String name, long userId) {
        StockRequest stockRequest = StockRequest.builder()
            .id(name)
            .name(name)
            .requesterId(userId)
            .status(RequestStatus.PENDING)
            .registerDate(new Date(System.currentTimeMillis()))
            .build();

        return stockRequestRepository.save(stockRequest);
    }



}

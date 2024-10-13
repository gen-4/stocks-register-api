package com.stocks.register.api.services.stock;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.stocks.register.api.dtos.stock.StockDto;
import com.stocks.register.api.dtos.stock.StockRequestDto;
import com.stocks.register.api.models.stock.Stock;
import com.stocks.register.api.repositories.stocks.StockRepository;

import lombok.RequiredArgsConstructor;




@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    @Override
    public List<StockDto> getAll() {
        return stockRepository.findAll().stream()
            .map(stock -> StockDto.builder()
                .id(stock.getId())
                .name(stock.getName())
                .build()
            )
            .collect(Collectors.toList());
    }

    @Override // TODO: This will store a request, it will not automatically save the stock
    public StockDto requestStock(StockRequestDto request) {
        Stock stock = Stock.builder()
            .id(request.getName())
            .name(request.getName())
            .build();
        Stock createdStock = stockRepository.save(stock);

        return StockDto.builder()
            .id(createdStock.getName())
            .name(createdStock.getName())
            .build();
    }



}

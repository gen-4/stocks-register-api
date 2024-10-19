package com.stocks.register.api.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import com.stocks.register.api.dtos.stock.StockDto;
import com.stocks.register.api.dtos.stock.StockRequestDto;
import com.stocks.register.api.models.stock.Stock;
import com.stocks.register.api.services.stock.StockService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;







@RestController
@CrossOrigin
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping("/stocks")
    public ResponseEntity<List<StockDto>> getAll() {
        return ResponseEntity.ok(stockService.getAll().stream()
            .map(stock -> StockDto.builder()
                .id(stock.getId())
                .name(stock.getName())
                .build()
            )
            .collect(Collectors.toList())
        );
    }

    @PostMapping("/stock") // TODO: This will store a request, it will not automatically save the stock
    public ResponseEntity<StockDto> requestStock(@RequestBody StockRequestDto request) {
        Stock stock = stockService.requestStock(request.getName());
        return ResponseEntity.ok(
            StockDto.builder()
            .id(stock.getName())
            .name(stock.getName())
            .build()
        );
    }
    
}

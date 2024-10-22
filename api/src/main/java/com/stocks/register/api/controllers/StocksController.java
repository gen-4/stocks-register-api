package com.stocks.register.api.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import com.stocks.register.api.dtos.stock.StockDto;
import com.stocks.register.api.dtos.stock.StockRequestDto;
import com.stocks.register.api.dtos.stock.StockRequestResponseDto;
import com.stocks.register.api.models.stock.StockRequest;
import com.stocks.register.api.services.stock.StockService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;







@RestController
@CrossOrigin
@RequestMapping("/stocks")
@RequiredArgsConstructor
public class StocksController {

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

    @PutMapping("/request")
    public ResponseEntity<StockRequestResponseDto> requestStock(
        @RequestBody StockRequestDto request,
        @RequestAttribute long userId
    ) {
        StockRequest stockRequest = stockService.requestStock(request.getName(), userId);

        return ResponseEntity.ok(
            StockRequestResponseDto.builder()
            .id(stockRequest.getId())
            .name(stockRequest.getName())
            .status(stockRequest.getStatus())
            .registerDate(stockRequest.getRegisterDate())
            .aprovalDate(stockRequest.getAprovalDate())
            .build()
        );
    }
    
}

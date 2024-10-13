package com.stocks.register.api.services.stock;

import java.util.List;

import com.stocks.register.api.dtos.stock.StockDto;
import com.stocks.register.api.dtos.stock.StockRequestDto;





public interface StockService {

    public List<StockDto> getAll();

    public StockDto requestStock(StockRequestDto request); // TODO: This will store a request, it will not automatically save the stock

}

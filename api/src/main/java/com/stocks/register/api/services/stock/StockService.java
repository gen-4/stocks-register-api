package com.stocks.register.api.services.stock;

import java.util.List;

import com.stocks.register.api.exceptions.DuplicatedEntityException;
import com.stocks.register.api.models.stock.Stock;
import com.stocks.register.api.models.stock.StockRequest;





public interface StockService {

    public List<Stock> getAll();

    public StockRequest requestStock(String name, long userId) throws DuplicatedEntityException;

}

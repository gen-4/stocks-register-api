package com.stocks.register.api.services.stock;

import java.util.List;

import com.stocks.register.api.models.stock.Stock;





public interface StockService {

    public List<Stock> getAll();

    public Stock requestStock(String name); // TODO: This will store a request, it will not automatically save the stock

}

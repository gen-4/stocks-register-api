package com.stocks.register.api.repositories.stocks;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.stocks.register.api.models.stock.Stock;




public interface StockRepository extends MongoRepository<Stock, String> {}

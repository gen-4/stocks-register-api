package com.stocks.register.api.repositories.stocks;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.stocks.register.api.models.stock.StockRequest;

public interface StockRequestRepository extends MongoRepository<StockRequest, String> {

    public List<StockRequest> findByRequesterId(long userId);
    
}

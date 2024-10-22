package com.stocks.register.api.models.stock;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;




@Document
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockRequest {
    
    @Id
    private String id;

    private long requesterId;
    
    private String name;

    private RequestStatus status;

    private long aproverId;

    private Date registerDate;
    
    private Date aprovalDate;
    
}

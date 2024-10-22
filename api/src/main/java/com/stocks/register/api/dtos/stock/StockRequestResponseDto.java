package com.stocks.register.api.dtos.stock;

import java.util.Date;

import com.stocks.register.api.models.stock.RequestStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockRequestResponseDto {

    private String id;

    private String name;

    private RequestStatus status;

    private Date registerDate;

    private Date aprovalDate;
    
}

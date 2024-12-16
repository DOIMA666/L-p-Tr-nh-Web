package com.shop.ecommerce.payload.data;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class FinanceData {
    private BigDecimal total;
    private Integer year;
    private Integer month;
    private Integer day;

    public FinanceData(BigDecimal total, Integer year, Integer month, Integer day) {
        this.total = total;
        this.year = year;
        this.month = month;
        this.day = day;
    }
}

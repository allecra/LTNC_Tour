package com.hoangminh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoucherCalculationRequest {
    private BigDecimal originalPrice;
    private String voucherCode;
}

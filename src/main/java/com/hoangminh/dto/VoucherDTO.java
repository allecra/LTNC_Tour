package com.hoangminh.dto;

import lombok.Data;

@Data
public class VoucherDTO {
    private Long id;
    private String code;
    private Double discount;
    private String expiry_date;
    private String dieuKien;
    private Boolean isUsed;
    private String receivedAt;
} 
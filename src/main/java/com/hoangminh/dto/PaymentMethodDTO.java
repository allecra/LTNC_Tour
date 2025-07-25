package com.hoangminh.dto;
import lombok.Data;

@Data
public class PaymentMethodDTO {
    private Long id;
    private String name;
    private String description;
    private String status;
} 
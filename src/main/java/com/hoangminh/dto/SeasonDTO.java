package com.hoangminh.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SeasonDTO {
    private Integer id;
    private String tenMua;
    private Integer thangBatDau;
    private Integer thangKetThuc;
}
package com.hoangminh.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TourTypeDTO {
    private Integer id;
    private String tenLoai;
    private String moTa;
}
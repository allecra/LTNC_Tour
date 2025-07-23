package com.hoangminh.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourStartDTO {
	private Long id;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date ngay_khoi_hanh;
	private Date ngay_ket_thuc;
	private Integer so_cho_con_lai;
	private BigDecimal gia_rieng;
	private Long tour_id;
}

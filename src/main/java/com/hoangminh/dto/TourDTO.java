package com.hoangminh.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hoangminh.entity.Image;
import com.hoangminh.entity.TourStart;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class TourDTO {

	private Long id;

	private String ten_tour;

	private String gioi_thieu_tour;

	private Integer so_ngay;

	private String noi_dung_tour;

	private String diem_den;

	private String loai_tour;

	private String anh_dai_dien;

	private String diem_khoi_hanh;

	private String trang_thai;

	private BigDecimal gia_tour;

	private BigDecimal sale_price;

	public TourDTO(Long id, String ten_tour, String gioi_thieu_tour, Integer so_ngay, String noi_dung_tour,
			String diem_den, String loai_tour, String anh_dai_dien, String diem_khoi_hanh, String trang_thai,
			BigDecimal gia_tour, BigDecimal sale_price) {
		this.id = id;
		this.ten_tour = ten_tour;
		this.gioi_thieu_tour = gioi_thieu_tour;
		this.so_ngay = so_ngay;
		this.noi_dung_tour = noi_dung_tour;
		this.diem_den = diem_den;
		this.loai_tour = loai_tour;
		this.anh_dai_dien = anh_dai_dien;
		this.diem_khoi_hanh = diem_khoi_hanh;
		this.trang_thai = trang_thai;
		this.gia_tour = gia_tour;
		this.sale_price = sale_price;
	}
}

package com.hoangminh.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import com.hoangminh.entity.Season;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tour_start", schema = "jsb_tour")
public class TourStart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "tour_id")
	private Tour tour;

	private Date ngay_khoi_hanh;

	private Date ngay_ket_thuc;

	private Integer so_cho_con_lai;

	private BigDecimal gia_rieng;

	private Integer month;

	private Integer year;

	@ManyToOne
	@JoinColumn(name = "season_id")
	private Season season;

}

package com.hoangminh.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import com.hoangminh.entity.TourType;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tour", schema = "jsb_tour")
public class Tour {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String ten_tour;

	@ManyToOne
	@JoinColumn(name = "destination_id")
	private Destination destination;

	private String diem_khoi_hanh;

	private String gioi_thieu_tour;

	private String noi_dung_tour;

	private String anh_dai_dien;

	private BigDecimal gia_tour;

	private BigDecimal sale_price;

	private Integer so_ngay;

	@ManyToOne
	@JoinColumn(name = "tour_type_id")
	private TourType tourType;

	private String trang_thai;

	@OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
	private List<TourStart> tour_starts;
}

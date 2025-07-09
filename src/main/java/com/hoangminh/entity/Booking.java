package com.hoangminh.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor @AllArgsConstructor @Getter @Setter
@Entity
@Table(name = "booking")
public class Booking {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    private Long user_id;
    
    private Long tour_id;
    
    private Integer so_luong_nguoi;
    
    private Date ngay_khoi_hanh;
    
    private Long tong_tien;
    
    private Integer trang_thai;
	
    private Date booking_at;

    private Integer pt_thanh_toan;

    private String ghi_chu;

    @PrePersist
    public void onCreate() {
    	this.booking_at = new Date();
    }
    
}

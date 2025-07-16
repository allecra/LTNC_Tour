package com.hoangminh.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "voucher")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ma_giam_gia", unique = true)
    private String maGiamGia;

    @Column(name = "gia_tri")
    private Double giaTri;

    @Column(name = "ngay_het_han")
    private Date ngayHetHan;

    @Column(name = "dieu_kien_ap_dung")
    private String dieuKienApDung;
}
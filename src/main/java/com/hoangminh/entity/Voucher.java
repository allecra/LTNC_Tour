package com.hoangminh.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "voucher", schema = "jsb_tour")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_giam_gia")
    private String maGiamGia;

    @Column(name = "gia_tri")
    private BigDecimal giaTri;

    @Column(name = "ngay_het_han")
    @Temporal(TemporalType.DATE)
    private Date ngayHetHan;

    @Column(name = "dieu_kien_ap_dung")
    private String dieuKienApDung;
}
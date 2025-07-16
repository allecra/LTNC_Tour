package com.hoangminh.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "season")
public class Season {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ten_mua")
    private String tenMua;

    @Column(name = "thang_bat_dau")
    private Integer thangBatDau;

    @Column(name = "thang_ket_thuc")
    private Integer thangKetThuc;
}
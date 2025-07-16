package com.hoangminh.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
public class VoucherDTO {
    private Integer id;
    private String maGiamGia;
    private Double giaTri;
    private Date ngayHetHan;
    private String dieuKienApDung;
}
package com.hoangminh.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
public class TinTucDTO {
    private Long id;
    private String tieuDe;
    private String tomTat;
    private String noiDung;
    private String hinhAnh;
    private Date ngayDang;
    private Long authorId;
    private String trangThai;
}
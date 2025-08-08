package com.hoangminh.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ReviewAdminDTO {
    private Long id;
    private String userName;
    private String tourName;
    private Integer rating;
    private String comment;
    private Date createdAt;
    private String trangThai;
    
    // Constructor cho query
    public ReviewAdminDTO(Long id, String userName, String tourName, 
                         Integer rating, String comment, Date createdAt, String trangThai) {
        this.id = id;
        this.userName = userName;
        this.tourName = tourName;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
        this.trangThai = trangThai;
    }
} 
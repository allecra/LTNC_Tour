package com.hoangminh.dto;
import lombok.Data;

@Data
public class ReviewAdminDTO {
    private Long id;
    private String userName;
    private String tourName;
    private String guideName;
    private Integer rating;
    private String comment;
    private String createdAt;
    private String trangThai;
} 
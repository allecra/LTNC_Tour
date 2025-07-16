package com.hoangminh.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
public class ReviewDTO {
    private Integer id;
    private Long tourId;
    private Long userId;
    private Integer rating;
    private String comment;
    private Date createdAt;
}
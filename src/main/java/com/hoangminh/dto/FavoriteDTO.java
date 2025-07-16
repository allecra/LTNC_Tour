package com.hoangminh.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FavoriteDTO {
    private Integer id;
    private Long userId;
    private Long tourId;
}
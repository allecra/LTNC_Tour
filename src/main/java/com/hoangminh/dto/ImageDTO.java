package com.hoangminh.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImageDTO {
    private Long id;
    private String url;
    private Long tourId;
    private String objectType;
    private Long objectId;
    private String caption;
}
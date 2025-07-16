package com.hoangminh.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DestinationDTO {
    private Long id;
    private String name;
    private String country;
    private Boolean isDomestic;
    private String description;
}
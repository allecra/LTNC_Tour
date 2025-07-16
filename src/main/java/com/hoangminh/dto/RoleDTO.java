package com.hoangminh.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleDTO {
    private Integer id;
    private String tenRole;
    private String moTa;
}
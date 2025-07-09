package com.hoangminh.dto;

import com.hoangminh.enumtype.USER_ROLE;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO {
	public String message;
	public Object data;
}

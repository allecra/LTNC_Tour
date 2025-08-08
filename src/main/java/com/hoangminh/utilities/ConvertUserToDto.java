package com.hoangminh.utilities;

import com.hoangminh.dto.UserDTO;
import com.hoangminh.entity.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConvertUserToDto {

	public static UserDTO convertUsertoDto(User user) {
		if (user == null) {
			log.warn("User is null in convertUsertoDto");
			return null;
		}
		
		try {
			UserDTO dto = new UserDTO(user.getId(), user.getUsername(), user.getHo_ten(), user.getSdt(), user.getGioi_tinh(),
					user.getEmail(),
					user.getDia_chi(), user.getRole().getId());
			log.info("Converted user {} to DTO with role_id: {}", user.getUsername(), user.getRole().getId());
			return dto;
		} catch (Exception e) {
			log.error("Error converting user to DTO: {}", e.getMessage(), e);
			return null;
		}
	}

}

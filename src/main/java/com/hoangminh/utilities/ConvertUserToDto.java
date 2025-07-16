package com.hoangminh.utilities;

import com.hoangminh.dto.UserDTO;
import com.hoangminh.entity.User;

public class ConvertUserToDto {

	public static UserDTO convertUsertoDto(User user) {
		Integer roleId = user.getRole() != null ? user.getRole().getId() : null;
		return new UserDTO(user.getId(), user.getUsername(), user.getHo_ten(), user.getSdt(), user.getGioi_tinh(),
				user.getEmail(),
				user.getDia_chi(), roleId);
	}

}

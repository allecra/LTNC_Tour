package com.hoangminh.controller.api.admin;

import com.hoangminh.service.UserService;
import com.hoangminh.utilities.SessionUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hoangminh.dto.LoginDTO;
import com.hoangminh.dto.ResponseDTO;
import com.hoangminh.dto.UserDTO;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	UserService userService;

	@PostMapping("/login")
	public ResponseDTO login(@RequestBody LoginDTO info) {
		if(this.userService.adminLogin(info)) {
			return new ResponseDTO("Thành công", SessionUtilities.getAdmin());
		}
		return new ResponseDTO("Thông tin đăng nhập không hợp lệ", null);
	}

	@GetMapping("/logout")
	public ResponseDTO logout() {
		this.userService.adminLogout();
		return new ResponseDTO("Đăng xuất thành công",null);
	}

}

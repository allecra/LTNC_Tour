package com.hoangminh.controller.api.admin;

import com.hoangminh.entity.User; 
import com.hoangminh.service.UserService;
import com.hoangminh.utilities.SessionUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hoangminh.dto.LoginDTO;
import com.hoangminh.dto.ResponseDTO;
import com.hoangminh.dto.UserDTO;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    UserService userService;

   @PostMapping("/login")
public ResponseDTO login(@RequestBody LoginDTO info) {
    User user = userService.findUserByUsername(info.getUsername()); // Đúng kiểu trả về
    if (user != null) {
        if (user.getRole().getId() == 1) { // Admin
            if (userService.adminLogin(info)) {
                Map<String, Object> result = new HashMap<>();
                result.put("role", "ADMIN");
                result.put("user", SessionUtilities.getAdmin());
                return new ResponseDTO("Thành công", result);
            }
        } else { // User thường
            if (userService.login(info)) {
                Map<String, Object> result = new HashMap<>();
                result.put("role", "USER");
                result.put("user", SessionUtilities.getUser());
                return new ResponseDTO("Thành công", result);
            }
        }
    }
    return new ResponseDTO("Thông tin đăng nhập không hợp lệ.", null);
}

    @GetMapping("/logout")
    public ResponseDTO logout() {
        // Xóa cả session user và admin để đảm bảo logout hoàn toàn
        com.hoangminh.utilities.SessionUtilities.setUser(null);
        com.hoangminh.utilities.SessionUtilities.setUsername(null);
        com.hoangminh.utilities.SessionUtilities.setAdmin(null);
        return new ResponseDTO("Đăng xuất thành công", null);
    }
}
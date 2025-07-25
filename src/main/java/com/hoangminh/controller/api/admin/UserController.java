package com.hoangminh.controller.api.admin;

import com.hoangminh.dto.ChangePasswordDTO;
import com.hoangminh.dto.ResponseDTO;
import com.hoangminh.dto.UpdateUserDTO;
import com.hoangminh.dto.UserDTO;
import com.hoangminh.entity.Role;
import com.hoangminh.entity.User;
import com.hoangminh.repository.RoleRepository;
import com.hoangminh.service.UserService;
import com.hoangminh.utilities.ConvertUserToDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/getAll")
    public ResponseDTO getAllUser(
            @RequestParam(value = "sdt", required = false) String sdt,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "ho_ten", required = false) String ho_ten,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam("pageIndex") Integer pageIndex) {
        if (!this.userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập", null);
        }

        Page<UserDTO> page = this.userService.findAllUser(sdt, email, ho_ten, PageRequest.of(pageIndex, pageSize));

        return new ResponseDTO("Thành công", page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseDTO getOneUser(@PathVariable("id") Long id) {

        if (!this.userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập", null);
        }

        if (this.userService.findUserById(id) != null) {
            return new ResponseDTO("Thành công", ConvertUserToDto.convertUsertoDto(this.userService.findUserById(id)));
        }
        return new ResponseDTO("Thất bại", null);
    }

    @PostMapping("")
    public ResponseDTO addUser(@RequestBody UserDTO userDTO) {
        if (!this.userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập", null);
        }
        // Kiểm tra username/email đã tồn tại chưa
        if (userService.findUserByUsername(userDTO.getUsername()) != null) {
            return new ResponseDTO("Tên đăng nhập đã tồn tại", null);
        }
        // Lấy role từ DB
        com.hoangminh.entity.Role role = roleRepository.findById(userDTO.getRole_id()).orElse(null);
        if (role == null) {
            return new ResponseDTO("Vai trò không hợp lệ", null);
        }
        // Tạo user mới
        com.hoangminh.entity.User user = new com.hoangminh.entity.User();
        user.setUsername(userDTO.getUsername());
        user.setHo_ten(userDTO.getHo_ten());
        user.setEmail(userDTO.getEmail());
        user.setSdt(userDTO.getSdt());
        user.setDia_chi(userDTO.getDia_chi());
        user.setRole(role);
        // Đặt mật khẩu mặc định (ví dụ: 123456, nhớ hash BCrypt)
        user.setPassword(org.mindrot.jbcrypt.BCrypt.hashpw("123456", org.mindrot.jbcrypt.BCrypt.gensalt(10)));
        user.setGioi_tinh(userDTO.getGioi_tinh() != null ? userDTO.getGioi_tinh() : "Khác");
        boolean ok = userService.saveUser(user);
        if (ok) {
            return new ResponseDTO("Thêm người dùng thành công", null);
        }
        return new ResponseDTO("Thêm người dùng thất bại", null);
    }

    @PutMapping("/update/{id}")
    public ResponseDTO updateUser(@PathVariable("id") Long id, @RequestBody UpdateUserDTO updateUserDTO) {

        if (!this.userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập", null);
        }

        User user = this.userService.findUserById(id);

        if (user != null) {
            if (this.userService.updateUserByAdmin(updateUserDTO, id)) {
                return new ResponseDTO("Cập nhật thành công", this.userService.findUserById(id));
            }
        }
        return new ResponseDTO("Cập nhật thất bại", null);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDTO deleteUser(@PathVariable("id") Long id) {

        if (!this.userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập", null);
        }

        User user = this.userService.findUserById(id);
        if (user != null) {

            if (this.userService.deleteUserById(id)) {
                return new ResponseDTO("Xóa thành công", null);
            }
        }

        return new ResponseDTO("Không thể xóa người dùng này", null);
    }

    @PutMapping("/update/resetPass/{id}")
    public ResponseDTO resetPass(@PathVariable("id") Long id) {

        if (!this.userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập", null);
        }

        if (this.userService.resetPass(id)) {
            return new ResponseDTO("Khôi phục mật khẩu mặc định thành công", null);
        }
        return new ResponseDTO("Khôi phục mật khẩu mặc định thất bại", null);

    }
}

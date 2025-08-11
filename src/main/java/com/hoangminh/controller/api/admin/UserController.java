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

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/getAll")
    public ResponseDTO getAllUser(
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "hoTen", required = false) String hoTen,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "sdt", required = false) String sdt,
            @RequestParam(value = "vaiTro", required = false) String vaiTro,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam("pageIndex") Integer pageIndex) {
        // Tạm thời bỏ qua kiểm tra admin để test
        // if (!this.userService.checkAdminLogin()) {
        //     return new ResponseDTO("Không có quyền truy cập", null);
        // }

        // Debug log
        System.out.println("Search term: " + searchTerm);
        System.out.println("Filters - hoTen: " + hoTen + ", email: " + email + ", sdt: " + sdt + ", vaiTro: " + vaiTro);

        Page<UserDTO> page = this.userService.findAllUserWithFilters(hoTen, email, sdt, vaiTro, PageRequest.of(pageIndex, pageSize));

        Map<String, Object> result = new HashMap<>();
        result.put("content", page.getContent());
        result.put("totalElements", page.getTotalElements());
        result.put("totalPages", page.getTotalPages());
        result.put("currentPage", page.getNumber());
        result.put("pageSize", page.getSize());

        return new ResponseDTO("Thành công", result);
    }
    
    @GetMapping("/getAllGuides")
    public ResponseDTO getAllGuides(
            @RequestParam(value = "hoTen", required = false) String hoTen,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "sdt", required = false) String sdt,
            @RequestParam(value = "gioiTinh", required = false) String gioiTinh,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam("pageIndex") Integer pageIndex) {
     
        // if (!this.userService.checkAdminLogin()) {
        //     return new ResponseDTO("Không có quyền truy cập", null);
        // }

        // Debug log
        System.out.println("Getting guides with filters - hoTen: " + hoTen + ", email: " + email + ", sdt: " + sdt + ", gioiTinh: " + gioiTinh);

        Page<UserDTO> page = this.userService.findAllGuidesWithFilters(hoTen, email, sdt, gioiTinh, PageRequest.of(pageIndex, pageSize));

        Map<String, Object> result = new HashMap<>();
        result.put("content", page.getContent());
        result.put("totalElements", page.getTotalElements());
        result.put("totalPages", page.getTotalPages());
        result.put("currentPage", page.getNumber());
        result.put("pageSize", page.getSize());

        return new ResponseDTO("Thành công", result);
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
    
    @GetMapping("/statistics")
    public ResponseDTO getUserStatistics() {
        // Tạm thời bỏ qua kiểm tra admin để test
        // if (!this.userService.checkAdminLogin()) {
        //     return new ResponseDTO("Không có quyền truy cập", null);
        // }
        
        try {
            List<User> allUsers = this.userService.getAllUsers();
            
            // Chỉ đếm user có role = 1 và 2
            long totalUsers = allUsers.stream().filter(u -> u.getRole().getId() == 1 || u.getRole().getId() == 2).count();
            long adminUsers = allUsers.stream().filter(u -> u.getRole().getId() == 1).count();
            long customerUsers = allUsers.stream().filter(u -> u.getRole().getId() == 2).count();
            
            Map<String, Object> statistics = new HashMap<>();
            statistics.put("totalUsers", totalUsers);
            statistics.put("adminUsers", adminUsers);
            statistics.put("customerUsers", customerUsers);
            
            return new ResponseDTO("Lấy thống kê người dùng thành công", statistics);
        } catch (Exception e) {
            return new ResponseDTO("Lỗi khi lấy thống kê: " + e.getMessage(), null);
        }
    }
    
    @GetMapping("/guide-statistics")
    public ResponseDTO getGuideStatistics() {
        // Tạm thời bỏ qua kiểm tra admin để test
        // if (!this.userService.checkAdminLogin()) {
        //     return new ResponseDTO("Không có quyền truy cập", null);
        // }
        
        try {
            List<User> allUsers = this.userService.getAllUsers();
            
            // Lọc hướng dẫn viên (role = 3)
            List<User> guides = allUsers.stream().filter(u -> u.getRole().getId() == 3).collect(Collectors.toList());
            
            long totalGuides = guides.size();
            long maleGuides = guides.stream().filter(g -> "Nam".equals(g.getGioi_tinh())).count();
            long femaleGuides = guides.stream().filter(g -> "Nữ".equals(g.getGioi_tinh())).count();
            long activeGuides = totalGuides; // Có thể thêm logic để xác định trạng thái hoạt động
            
            Map<String, Object> statistics = new HashMap<>();
            statistics.put("totalGuides", totalGuides);
            statistics.put("maleGuides", maleGuides);
            statistics.put("femaleGuides", femaleGuides);
            statistics.put("activeGuides", activeGuides);
            
            return new ResponseDTO("Lấy thống kê hướng dẫn viên thành công", statistics);
        } catch (Exception e) {
            return new ResponseDTO("Lỗi khi lấy thống kê hướng dẫn viên: " + e.getMessage(), null);
        }
    }
    

}

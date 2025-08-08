package com.hoangminh.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.hoangminh.dto.*;
import com.hoangminh.repository.BookingRepository;
import com.hoangminh.repository.RoleRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.hoangminh.controller.HomeController;
import com.hoangminh.entity.User;
import com.hoangminh.repository.UserRepository;
import com.hoangminh.service.UserService;
import com.hoangminh.utilities.ConvertUserToDto;
import com.hoangminh.utilities.SessionUtilities;
import jakarta.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;
import com.hoangminh.entity.Role;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private RoleRepository roleRepository;


	
	@Override
	public Page<UserDTO> findAllUserWithFilters(String hoTen, String email, String sdt, String vaiTro, Pageable pageable) {

		log.info("findAllUserWithFilters called with - hoTen: {}, email: {}, sdt: {}, vaiTro: {}", hoTen, email, sdt, vaiTro);
		
		Page<User> page = userRepository.findAllWithFilters(hoTen, email, sdt, vaiTro, pageable);
		
		log.info("Repository returned {} users, total: {}", page.getContent().size(), page.getTotalElements());

		Page<UserDTO> pageUserDTO = new PageImpl<>(
				page.getContent().stream().map(user -> {
					UserDTO userDTO = ConvertUserToDto.convertUsertoDto(user);
					log.info("Converted user: {} -> {}", user.getUsername(), userDTO.getUsername());
					return userDTO;
				}).collect(Collectors.toList()),
				page.getPageable(),
				page.getTotalElements());

		log.info("Returning {} DTOs", pageUserDTO.getContent().size());
		return pageUserDTO;
	}
	
	@Override
	public Page<UserDTO> findAllGuidesWithFilters(String hoTen, String email, String sdt, String gioiTinh, Pageable pageable) {

		log.info("findAllGuidesWithFilters called with - hoTen: {}, email: {}, sdt: {}, gioiTinh: {}", hoTen, email, sdt, gioiTinh);
		
		Page<User> page = userRepository.findAllGuidesWithFilters(hoTen, email, sdt, gioiTinh, pageable);
		
		log.info("Repository returned {} guides, total: {}", page.getContent().size(), page.getTotalElements());

		Page<UserDTO> pageUserDTO = new PageImpl<>(
				page.getContent().stream().map(user -> {
					UserDTO userDTO = ConvertUserToDto.convertUsertoDto(user);
					log.info("Converted guide: {} -> {}", user.getUsername(), userDTO.getUsername());
					return userDTO;
				}).collect(Collectors.toList()),
				page.getPageable(),
				page.getTotalElements());

		log.info("Returning {} DTOs", pageUserDTO.getContent().size());
		return pageUserDTO;
	}
	


	@Override
	public User findUserById(Long id) {
		Optional<User> user = userRepository.findById(id);
		if (user.isPresent()) {
			return user.get();
		}
		return null;
	}

	@Override
	public User findUserByUsername(String username) {
		User user = userRepository.findByUsername(username);
		if (user != null) {
			return user;
		}
		return null;
	}

	@Override
	public boolean saveUser(User user) {
		// Để JPA tự sinh id, không set id thủ công
		if (this.userRepository.save(user) != null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean updateUser(UpdateUserDTO updateUserDTO) {
		if (SessionUtilities.getUser() != null) {
			Long user_id = SessionUtilities.getUser().getId();

			User user = this.userRepository.findById(user_id).get();

			user.setSdt(updateUserDTO.getSdt());
			user.setUsername(updateUserDTO.getUsername());
			user.setEmail(updateUserDTO.getEmail());
			user.setDia_chi(updateUserDTO.getDia_chi());
			user.setHo_ten(updateUserDTO.getHo_ten());
			user.setGioi_tinh(updateUserDTO.getGioi_tinh());

			this.userRepository.save(user);

			SessionUtilities.setUser(ConvertUserToDto.convertUsertoDto(user));

			return true;

		}

		return false;
	}

	@Override
	public boolean deleteUserById(Long id) {
		Optional<User> user = this.userRepository.findById(id);
		if (user.isPresent()) {
			this.userRepository.deleteById(id);
			return true;
		}
		return false;
	}

	@Override
	public boolean login(LoginDTO user) {

		User userCheck = this.findUserByUsername(user.getUsername());

		if (userCheck == null) {
			return false;
		}

		log.info("userCheck:{}", userCheck.getUsername());

		if (BCrypt.checkpw(user.getPassword(), userCheck.getPassword())) {
			SessionUtilities.setUsername(userCheck.getUsername());
			SessionUtilities.setUser(ConvertUserToDto.convertUsertoDto(userCheck));

			log.info("userCheck:{}", SessionUtilities.getUsername());
			return true;
		}

		return false;
	}

	@Override
	public boolean register(RegisterDTO newUser) {
		User userCheckByUserName = this.findUserByUsername(newUser.getUsername());
		User userCheckByEmail = this.userRepository.getUserByEmail(newUser.getEmail());
		if (userCheckByUserName != null || userCheckByEmail != null) {
			return false;
		}
		User user = new User();
		user.setUsername(newUser.getUsername());
		user.setHo_ten(newUser.getHo_ten());
		user.setPassword(BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt(10)));
		user.setEmail(newUser.getEmail());
		user.setGioi_tinh(newUser.getGioi_tinh());
		user.setDia_chi(newUser.getDia_chi());
		user.setSdt(newUser.getSdt());
		// Set createdAt sẽ tự động nhờ @PrePersist
		Optional<Role> roleOpt = this.roleRepository.findById(2L);
		if (!roleOpt.isPresent()) {
			return false;
		}
		user.setRole(roleOpt.get());
		return this.saveUser(user);
	}

	@Override
	public boolean checkLogin() {
		return SessionUtilities.getUsername() != null;
	}

	@Override
	public boolean changePassword(ChangePasswordDTO changePasswordDTO) {
		if (SessionUtilities.getUser() != null) {
			Long user_id = SessionUtilities.getUser().getId();

			User user = this.userRepository.findById(user_id).get();

			if (BCrypt.checkpw(changePasswordDTO.getOldPassword(), user.getPassword())
					&& changePasswordDTO.getNewPassword() != null) {
				user.setPassword(BCrypt.hashpw(changePasswordDTO.getNewPassword(), BCrypt.gensalt(10)));
				this.userRepository.save(user);
				return true;
			}
			return false;

		}
		return false;
	}

	@Override
	public boolean updateUserByAdmin(UpdateUserDTO updateUserDTO, Long id) {

		User user = this.userRepository.findById(id).get();
		if (user != null) {
			user.setSdt(updateUserDTO.getSdt());
			user.setUsername(updateUserDTO.getUsername());
			user.setEmail(updateUserDTO.getEmail());
			user.setDia_chi(updateUserDTO.getDia_chi());
			user.setHo_ten(updateUserDTO.getHo_ten());
			user.setGioi_tinh(updateUserDTO.getGioi_tinh());

			this.userRepository.save(user);

			return true;
		}

		return false;
	}

	@Override
	public boolean adminLogin(LoginDTO user) {
		User userCheck = this.findUserByUsername(user.getUsername());

		if (userCheck == null) {
			return false;
		}

		log.info("userCheck:{}", userCheck.getUsername());

		if (BCrypt.checkpw(user.getPassword(), userCheck.getPassword()) && userCheck.getRole().getId() == 1) {

			SessionUtilities.setAdmin(ConvertUserToDto.convertUsertoDto(userCheck));

			log.info("userCheck:{}", SessionUtilities.getAdmin().getUsername());

			return true;
		}

		return false;
	}

	@Override
	public boolean checkAdminLogin() {
		return SessionUtilities.getAdmin() != null;
	}

	@Override
	public void adminLogout() {
		SessionUtilities.setAdmin(null);
	}

	@Override
	public boolean resetPass(Long id) {
		User user = this.userRepository.findById(id).get();

		user.setPassword(BCrypt.hashpw("123@123a", BCrypt.gensalt(10)));

		if (this.userRepository.save(user) != null) {
			return true;
		}

		return false;
	}

	@Override
	public UserDTO findUserDTOById(Long id) {
		User user = this.userRepository.findById(id).orElse(null);
		if (user != null) {
			return ConvertUserToDto.convertUsertoDto(user);
		}
		return null;
	}

	@Override
	public String getRegisterError(RegisterDTO newUser) {
		if (this.findUserByUsername(newUser.getUsername()) != null) {
			return "Tên người dùng đã tồn tại";
		}
		if (this.userRepository.getUserByEmail(newUser.getEmail()) != null) {
			return "Email đã tồn tại";
		}
		if (!this.roleRepository.findById(2L).isPresent()) {
			return "Không tìm thấy quyền khách hàng";
		}
		boolean ok = this.register(newUser);
		if (!ok) {
			return "Đăng ký thất bại do lỗi hệ thống";
		}
		return null;
	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}
	
	@Override
	public List<User> getAllGuides() {
		// Sử dụng repository method để lấy trực tiếp guides (role_id = 3)
		return userRepository.findAllGuidesWithFilters(null, null, null, null, Pageable.unpaged()).getContent();
	}
	
	@Override
	public String encodePassword(String rawPassword) {
		return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
	}
}

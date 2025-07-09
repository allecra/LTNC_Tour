package com.hoangminh.service;

import java.util.List;
import java.util.Optional;

import com.hoangminh.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hoangminh.entity.User;
import org.springframework.data.repository.query.Param;

public interface UserService {
	public Page<UserDTO> findAllUser(String sdt,String email,String ho_ten, Pageable pageable);
	public User findUserById(Long id);
	public User findUserByUsername(String username);
	public boolean saveUser(User user);
	public boolean login(LoginDTO user);
	public boolean register(RegisterDTO user);
	public boolean updateUser(UpdateUserDTO updateUserDTO);
	public boolean deleteUserById(Long id);
	public boolean checkLogin();
	public boolean changePassword(ChangePasswordDTO changePasswordDTO);
	public boolean updateUserByAdmin(UpdateUserDTO updateUserDTO,Long id);
	public boolean adminLogin(LoginDTO user);
	public boolean checkAdminLogin();
	public void adminLogout();
	public boolean resetPass(Long id);
}

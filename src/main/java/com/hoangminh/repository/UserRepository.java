package com.hoangminh.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hoangminh.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query(value = "select u from User u where u.id = :id")
	Optional<User> findById(@Param("id") Long id);

	@Query(value = "select u from User u where u.username = :username")
	User findByUsername(@Param("username") String username);

	@Query(value = "select u from User u where u.email = :email")
	User getUserByEmail(@Param("email") String email);

	@Query("SELECT MAX(u.id) FROM User u")
	Long findMaxId();
	
	@Query(value = "select u from User u " +
			"WHERE ( :hoTen IS NULL OR :hoTen = '' OR u.ho_ten LIKE %:hoTen% ) " +
			"AND ( :email IS NULL OR :email = '' OR u.email LIKE %:email% ) " +
			"AND ( :sdt IS NULL OR :sdt = '' OR u.sdt LIKE %:sdt% ) " +
			"AND ( :vaiTro IS NULL OR :vaiTro = '' OR u.role.ten_role = :vaiTro ) " +
			"AND u.role.id IN (1, 2) " +
			"ORDER BY u.id desc")
	Page<User> findAllWithFilters(@Param("hoTen") String hoTen, @Param("email") String email, 
								  @Param("sdt") String sdt, @Param("vaiTro") String vaiTro, Pageable pageable);
	
	@Query(value = "select u from User u " +
			"WHERE ( :hoTen IS NULL OR :hoTen = '' OR u.ho_ten LIKE %:hoTen% ) " +
			"AND ( :email IS NULL OR :email = '' OR u.email LIKE %:email% ) " +
			"AND ( :sdt IS NULL OR :sdt = '' OR u.sdt LIKE %:sdt% ) " +
			"AND ( :gioiTinh IS NULL OR :gioiTinh = '' OR u.gioi_tinh = :gioiTinh ) " +
			"AND u.role.id = 3 " +
			"ORDER BY u.id desc")
	Page<User> findAllGuidesWithFilters(@Param("hoTen") String hoTen, @Param("email") String email, 
										@Param("sdt") String sdt, @Param("gioiTinh") String gioiTinh, Pageable pageable);
}

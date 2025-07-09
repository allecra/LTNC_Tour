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
public interface UserRepository extends JpaRepository<User, Long>{
	
		@Query(value="select u from User u " +
				"WHERE ( :sdt IS NULL OR :sdt = '' OR u.sdt LIKE %:sdt% ) " +
				"AND ( :email IS NULL OR :email = '' OR u.email LIKE %:email% ) " +
				"AND ( :ho_ten IS NULL OR :ho_ten = '' OR u.ho_ten LIKE %:ho_ten% ) " +
				"AND u.role = 1 " +
				" ORDER BY u.id desc")
		Page<User> findAll(@Param("sdt") String sdt,@Param("email") String email,@Param("ho_ten") String ho_ten,Pageable pageable);
	
		@Query(value="select u from User u where u.id = :id")
		Optional<User> findById(@Param("id") Long id);

		@Query(value="select u from User u where u.username = :username")
		User findByUsername(@Param("username")String username);
		
		@Query(value="select u from User u where u.email = :email")
		User getUserByEmail(@Param("email") String email);


		
}

package com.hoangminh.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hoangminh.enumtype.USER_ROLE;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
@Entity
@Table(name = "user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String username;
	
	private String ho_ten;
	
	@JsonIgnore
	private String password;
	
	private String gioi_tinh;
	
	private String sdt;
	
	private String email;
	
	private String dia_chi;

	private Integer role;
}

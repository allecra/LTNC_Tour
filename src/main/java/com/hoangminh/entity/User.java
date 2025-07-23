package com.hoangminh.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hoangminh.enumtype.USER_ROLE;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user", schema = "jsb_dulich")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;

	private String ho_ten;

	@JsonIgnore
	@Column(name = "pass")
	private String password;

	private String gioi_tinh;

	private String sdt;

	private String email;

	private String dia_chi;

	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at")
	private Date createdAt;

	@PrePersist
	protected void onCreate() {
		this.createdAt = new Date();
	}
}

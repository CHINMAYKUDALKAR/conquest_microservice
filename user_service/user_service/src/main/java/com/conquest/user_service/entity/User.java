package com.conquest.user_service.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name="user", uniqueConstraints = {@UniqueConstraint(columnNames = {"email", "phone"})})
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false, unique = true)
	private String phone;
	@Column(nullable = false, unique = true)
	private String email;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;
	
	public User() {}

	public User(String name, String phone, String email, Role role) {
		super();
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.role = role;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", phone=" + phone + ", email=" + email + ", role=" + role + "]";
	}
	
}

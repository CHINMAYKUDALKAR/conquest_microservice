package com.conquest.user_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.conquest.user_service.entity.Role;
import com.conquest.user_service.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	
	@Query("select user from User user where user.email=:email")
	Optional<User> getUserByEmail(@Param("email") String email);
	
	@Query("select user from User user where user.phone=:phone")
	Optional<User> getUserByPhone(@Param("phone") String phone);

	@Query("select user from User user where user.role=:role")
	List<User> getUsersByRole(@Param("role") Role role);
	
}
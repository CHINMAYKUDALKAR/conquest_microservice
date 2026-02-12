package com.conquest.user_service.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.conquest.user_service.entity.Role;
import com.conquest.user_service.entity.User;
import com.conquest.user_service.exception.UserServiceException;
import com.conquest.user_service.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public User createUser(User user) throws RuntimeException{
		return userRepository.save(user);
	}

	@Override
	public User getUserById(Integer userId) {
		return userRepository.findById(userId)
			.orElseThrow(()->new UserServiceException("Not found"));
	}

	@Override
	public User getUserByEmail(String email) {
		return userRepository.getUserByEmail(email)
			.orElseThrow(()->new UserServiceException("Not found"));
	}

	@Override
	public User getUserByPhone(String phone) {
		return userRepository.getUserByPhone(phone)
			.orElseThrow(()->new UserServiceException("Not found"));
	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public List<User> getAllExecutives() {
		return userRepository.getUsersByRole(Role.EXECUTIVE);
	}

	@Override
	public User updateUser(User user) {
		User existingUser = userRepository.findById(user.getId())
	            .orElseThrow(() -> new UserServiceException("Not found"));
	    existingUser.setName(user.getName());
	    existingUser.setEmail(user.getEmail());
	    existingUser.setPhone(user.getPhone());
	    existingUser.setRole(user.getRole());
	    return userRepository.save(existingUser);
	}

	@Override
	public void deleteUser(Integer userId) {
		userRepository.deleteById(userId);
	}

	@Override
	public List<User> getAllCustomers() {
		return userRepository.getUsersByRole(Role.CUSTOMER);
	}

}
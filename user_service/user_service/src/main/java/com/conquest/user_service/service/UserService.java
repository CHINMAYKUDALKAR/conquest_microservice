package com.conquest.user_service.service;

import java.util.List;
import com.conquest.user_service.entity.User;

public interface UserService {
	User createUser(User user)throws RuntimeException;
    User getUserById(Integer userId);
    User getUserByEmail(String email);
    User getUserByPhone(String phone);
    List<User> getAllUsers();
    List<User> getAllCustomers();
    List<User> getAllExecutives();
    User updateUser(User user);
    void deleteUser(Integer userId);
}
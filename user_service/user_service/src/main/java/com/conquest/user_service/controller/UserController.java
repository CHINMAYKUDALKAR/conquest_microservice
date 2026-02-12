package com.conquest.user_service.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.conquest.user_service.entity.User;
import com.conquest.user_service.exception.UserServiceException;
import com.conquest.user_service.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@PostMapping
	public ResponseEntity<User> createUser(@RequestBody User user) {
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
		}catch(RuntimeException e) {
			return new ResponseEntity<>(new User(),HttpStatus.CONFLICT);
		}
	}
	
	@GetMapping("by-id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Integer userId) {
    	return new ResponseEntity<>(userService.getUserById(userId),HttpStatus.OK);
    }
	
	@GetMapping("by-email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable("email") String email) {
		return new ResponseEntity<>(userService.getUserByEmail(email),HttpStatus.OK);
	}
	
	@GetMapping("by-phone/{phone}")
    public ResponseEntity<User> getUserByPhone(@PathVariable("phone") String phone) {
		return new ResponseEntity<>(userService.getUserByPhone(phone),HttpStatus.OK);
	}
	
	@GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
		return new ResponseEntity<>(userService.getAllUsers(),HttpStatus.OK);
	}
	
	@GetMapping("/customer")
    public ResponseEntity<List<User>> getAllCustomers(){
		return new ResponseEntity<>(userService.getAllCustomers(),HttpStatus.OK);
	}
	
	@GetMapping("/executive")
    public ResponseEntity<List<User>> getAllExecutives(){
		return new ResponseEntity<>(userService.getAllExecutives(), HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Integer id, @RequestBody User user) {
		return new ResponseEntity<>(userService.updateUser(user),HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
	    userService.deleteUser(id);
	    return ResponseEntity.noContent().build(); 
	}
}
package com.conquest.ticket_service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.conquest.ticket_service.entity.UserDTO;

@Service
public class UserServiceClient {
	@Autowired
	private RestTemplate restTemplate;
	
	public UserDTO getUserById(Integer userId) {
	    return restTemplate.getForObject("http://user-micro-service/users/by-id/" + userId,UserDTO.class);
	}
}

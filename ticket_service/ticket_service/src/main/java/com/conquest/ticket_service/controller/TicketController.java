package com.conquest.ticket_service.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.conquest.ticket_service.client.UserServiceClient;
import com.conquest.ticket_service.entity.Ticket;
import com.conquest.ticket_service.entity.UserDTO;
import com.conquest.ticket_service.service.TicketService;

@RestController
@RequestMapping("/tickets")
public class TicketController {
	
	@Autowired
	private TicketService ticketService;
	
	@PostMapping
	public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket) {
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.createTicket(ticket));
		}catch(RuntimeException e) {
			return new ResponseEntity<>(new Ticket(),HttpStatus.CONFLICT);
		}
	}
	
	@GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable("id") Integer ticketId) {
    	return new ResponseEntity<>(ticketService.getTicketById(ticketId),HttpStatus.OK);
    }
	
	@GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets(){
		return new ResponseEntity<>(ticketService.getAllTickets(),HttpStatus.OK);
	}
	
	@PutMapping
    public ResponseEntity<Ticket> updateTicket(@RequestBody Ticket ticket) {
		return new ResponseEntity<>(ticketService.updateTicket(ticket),HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTicket(@PathVariable Integer id) {
	    ticketService.deleteTicket(id);
	    return ResponseEntity.noContent().build(); 
	}
	
	@PatchMapping("/{id}")
    public ResponseEntity<Ticket> closeTicket(@PathVariable("id") Integer id) {
		return new ResponseEntity<>(ticketService.closeTicket(id),HttpStatus.OK);
	}
	
	@PatchMapping("/{ticketId}/{executiveId}")
    public ResponseEntity<Ticket> assignTicket(@PathVariable("ticketId") Integer ticketId, @PathVariable("executiveId") Integer executiveId) {
		return new ResponseEntity<>(ticketService.assignTicket(ticketId,executiveId),HttpStatus.OK);
	}
	
	@Autowired
	private UserServiceClient userServiceClient;
	
	@GetMapping("/userdto/{userId}")
	public ResponseEntity<UserDTO> getUser(@PathVariable("userId")Integer userId) {
		//System.out.println(userServiceClient.getUserById(userId));
		return new ResponseEntity<>(userServiceClient.getUserById(userId),HttpStatus.OK);
	}
	
	//Get tickets raised by user
	@GetMapping("/users/{userId}")
	public List<Ticket> getTicketsRaisedByUser(@PathVariable("userId")Integer userId) {
		return ticketService.findTicketsRaisedByUser(userId);
	}
}

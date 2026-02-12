package com.conquest.ticket_service.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.conquest.ticket_service.entity.Status;
import com.conquest.ticket_service.entity.Ticket;
import com.conquest.ticket_service.exception.TicketServiceException;
import com.conquest.ticket_service.repository.TicketRepository;

@Service
public class TicketServiceImpl implements TicketService{
	
	@Autowired
	private TicketRepository ticketRepository;

	@Override
	public Ticket assignTicket(Integer ticketId, Integer executiveId) {
		Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        ticket.setAssignedTo(executiveId);
        ticket.setStatus(Status.ASSIGNED);
        return ticketRepository.save(ticket);
	}

	@Override
	public Ticket closeTicket(Integer ticketId) {
		Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        ticket.setStatus(Status.CLOSED);
        return ticketRepository.save(ticket);
	}

	@Override
	public Ticket createTicket(Ticket ticket) throws RuntimeException {
		return ticketRepository.save(ticket);
	}

	@Override
	public Ticket getTicketById(Integer ticketId) {
		return ticketRepository.findById(ticketId)
				.orElseThrow(()->new TicketServiceException("Not found"));
	}

	@Override
	public List<Ticket> getAllTickets() {
		return ticketRepository.findAll();
	}

	@Override
	public Ticket updateTicket(Ticket ticket) {
		Ticket existingTicket = ticketRepository.findById(ticket.getId())
	            .orElseThrow(() -> new TicketServiceException("Not found"));
	    existingTicket.setIssue(existingTicket.getIssue());
	    existingTicket.setRaisedBy(existingTicket.getRaisedBy());
	    existingTicket.setRaisedOn(existingTicket.getRaisedOn());
	    existingTicket.setAssignedTo(existingTicket.getAssignedTo());
	    existingTicket.setAssignedOn(existingTicket.getAssignedOn());
	    existingTicket.setClosedOn(existingTicket.getClosedOn());
	    existingTicket.setStatus(existingTicket.getStatus());
	    return ticketRepository.save(existingTicket);
	}

	@Override
	public void deleteTicket(Integer ticketId) {
		ticketRepository.deleteById(ticketId);
	}

	@Override
	public List<Ticket> findTicketsRaisedByUser(Integer userId) {
		return ticketRepository.findByCreatedBy(userId);
	}

}

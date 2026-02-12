package com.conquest.ticket_service.service;

import java.util.List;
import com.conquest.ticket_service.entity.Ticket;

public interface TicketService {
	List<Ticket> findTicketsRaisedByUser(Integer userId);
    Ticket assignTicket(Integer ticketId, Integer executiveId);
    Ticket closeTicket(Integer ticketId);
    Ticket createTicket(Ticket ticket)throws RuntimeException;
    Ticket getTicketById(Integer ticketId);
    List<Ticket> getAllTickets();
    Ticket updateTicket(Ticket ticket);
    void deleteTicket(Integer ticketId);
}
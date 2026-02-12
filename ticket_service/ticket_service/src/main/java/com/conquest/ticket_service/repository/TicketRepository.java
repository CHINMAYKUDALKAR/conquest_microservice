package com.conquest.ticket_service.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.conquest.ticket_service.entity.Status;
import com.conquest.ticket_service.entity.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer>{
	
	@Query("select t from Ticket t where t.raisedBy = :userId")
	List<Ticket> findByCreatedBy(@Param("userId") Integer userId);
	
	@Query("select t from Ticket t where t.status = :status")
	List<Ticket> findByStatus(@Param("status") Status status);
	
	@Query("select t from Ticket t where t.assignedTo = :executiveId")
	List<Ticket> findByAssignedTo(@Param("executiveId") Integer executiveId);
    
}
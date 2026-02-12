package com.conquest.ticket_service.entity;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Enumerated;

@Entity
@Table(name="ticket")
public class Ticket {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String issue;
	private Integer raisedBy;
	private LocalDate raisedOn;
	private Integer assignedTo;
	private LocalDate assignedOn;
	private LocalDate closedOn;
	@Enumerated(EnumType.STRING)
	private Status status;
	
	public Ticket() {}

	public Ticket(String issue, Integer raisedBy, LocalDate raisedOn, Integer assignedTo, LocalDate assignedOn, LocalDate closedOn, Status status) {
		super();
		this.issue = issue;
		this.raisedBy = raisedBy;
		this.raisedOn = raisedOn;
		this.assignedTo = assignedTo;
		this.assignedOn = assignedOn;
		this.closedOn = closedOn;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public Integer getRaisedBy() {
		return raisedBy;
	}

	public void setRaisedBy(Integer raisedBy) {
		this.raisedBy = raisedBy;
	}

	public LocalDate getRaisedOn() {
		return raisedOn;
	}

	public void setRaisedOn(LocalDate raisedOn) {
		this.raisedOn = raisedOn;
	}

	public Integer getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(Integer assignedTo) {
		this.assignedTo = assignedTo;
	}

	public LocalDate getAssignedOn() {
		return assignedOn;
	}

	public void setAssignedOn(LocalDate assignedOn) {
		this.assignedOn = assignedOn;
	}

	public LocalDate getClosedOn() {
		return closedOn;
	}

	public void setClosedOn(LocalDate closedOn) {
		this.closedOn = closedOn;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Ticket [id=" + id + ", issue=" + issue + ", raisedBy=" + raisedBy + ", raisedOn=" + raisedOn
				+ ", assignedTo=" + assignedTo + ", assignedOn=" + assignedOn + ", closedOn=" + closedOn + ", status="
				+ status + "]";
	}
	
}

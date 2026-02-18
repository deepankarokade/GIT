package com.git.Student.Service;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.git.Student.Entity.Student;
import com.git.Student.Entity.SupportTicket;
import com.git.Student.Repository.SupportTicketRepository;

@Service
public class SupportTicketService {
    @Autowired
    private SupportTicketRepository repository;

    public SupportTicket saveTicket(SupportTicket ticket) {
        ticket.setStatus("OPEN");
        ticket.setCreatedDate(LocalDateTime.now());
        ticket.setUpdatedDate(LocalDateTime.now());
        return repository.save(ticket);
    }

    public List<SupportTicket> getAllTickets() {
        return repository.findAll();
    }

    public List<SupportTicket> getTicketsByStudent(Student student) {
        return repository.findByStudentOrderByCreatedDateDesc(student);
    }

    public SupportTicket respondToTicket(Long ticketId, String response) {
        Optional<SupportTicket> optional = repository.findById(ticketId);
        if (optional.isPresent()) {
            SupportTicket ticket = optional.get();
            ticket.setAdminResponse(response);
            ticket.setAdminResponseDate(LocalDateTime.now());
            ticket.setStatus("RESOLVED");
            ticket.setUpdatedDate(LocalDateTime.now());
            return repository.save(ticket);
        }
        return null;
    }
}

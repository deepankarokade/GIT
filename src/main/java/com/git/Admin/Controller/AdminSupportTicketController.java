package com.git.Admin.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.git.Student.Entity.SupportTicket;
import com.git.Student.Service.SupportTicketService;

@RestController
@RequestMapping("/api/admin/support-tickets")
public class AdminSupportTicketController {

    @Autowired
    private SupportTicketService supportTicketService;

    @GetMapping
    public ResponseEntity<List<SupportTicket>> getAllTickets() {
        return ResponseEntity.ok(supportTicketService.getAllTickets());
    }

    @PostMapping("/{ticketId}/respond")
    public ResponseEntity<SupportTicket> respondToTicket(
            @PathVariable Long ticketId,
            @RequestBody Map<String, String> request) {

        String response = request.get("response");
        SupportTicket updatedTicket = supportTicketService.respondToTicket(ticketId, response);

        if (updatedTicket != null) {
            return ResponseEntity.ok(updatedTicket);
        }
        return ResponseEntity.notFound().build();
    }
}

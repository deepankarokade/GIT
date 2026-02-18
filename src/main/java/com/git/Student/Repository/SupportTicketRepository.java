package com.git.Student.Repository;

import java.util.List;
import com.git.Student.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import com.git.Student.Entity.SupportTicket;

public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
    List<SupportTicket> findByStudentOrderByCreatedDateDesc(Student student);
}
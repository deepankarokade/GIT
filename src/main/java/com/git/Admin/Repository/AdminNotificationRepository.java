package com.git.Admin.Repository;

import com.git.Admin.Entity.AdminNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminNotificationRepository extends JpaRepository<AdminNotification, Long> {
    List<AdminNotification> findByIsReadFalseOrderByCreatedAtDesc();

    List<AdminNotification> findTop10ByOrderByCreatedAtDesc();

    long countByIsReadFalse();
}

package com.git.Admin.Service;

import com.git.Admin.Entity.AdminNotification;
import com.git.Admin.Repository.AdminNotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminNotificationService {

    private final AdminNotificationRepository notificationRepository;

    public AdminNotificationService(AdminNotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void createNotification(String message, String type, String link) {
        AdminNotification notification = new AdminNotification(message, type, link);
        notificationRepository.save(notification);
    }

    public List<AdminNotification> getUnreadNotifications() {
        return notificationRepository.findByIsReadFalseOrderByCreatedAtDesc();
    }

    public List<AdminNotification> getRecentNotifications() {
        return notificationRepository.findTop10ByOrderByCreatedAtDesc();
    }

    public long getUnreadCount() {
        return notificationRepository.countByIsReadFalse();
    }

    public void markAsRead(Long id) {
        if (id == null)
            return;
        notificationRepository.findById(id).ifPresent(n -> {
            n.setRead(true);
            notificationRepository.save(n);
        });
    }

    public void markAllAsRead() {
        List<AdminNotification> unread = notificationRepository.findByIsReadFalseOrderByCreatedAtDesc();
        unread.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(unread);
    }
}

package com.git.Admin.Controller;

import com.git.Admin.Entity.AdminNotification;
import com.git.Admin.Service.AdminNotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/api/notifications")
public class AdminNotificationController {

    private final AdminNotificationService notificationService;

    public AdminNotificationController(AdminNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/unread")
    public List<AdminNotification> getUnread() {
        return notificationService.getUnreadNotifications();
    }

    @GetMapping("/recent")
    public List<AdminNotification> getRecent() {
        return notificationService.getRecentNotifications();
    }

    @GetMapping("/unread-count")
    public Map<String, Long> getUnreadCount() {
        return Map.of("count", notificationService.getUnreadCount());
    }

    @PostMapping("/{id}/mark-as-read")
    public void markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
    }

    @PostMapping("/mark-all-as-read")
    public void markAllAsRead() {
        notificationService.markAllAsRead();
    }
}

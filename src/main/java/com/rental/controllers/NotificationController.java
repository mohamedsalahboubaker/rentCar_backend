// com/rental/controllers/NotificationController.java
package com.rental.controllers;

import com.rental.model.Notification;
import com.rental.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")  // ✅ Enlever /api (car déjà dans le context-path)
@CrossOrigin(origins = "*")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification) {
        try {
            Notification created = notificationService.createNotification(notification);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        List<Notification> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/destinataire/{destinataire}")
    public ResponseEntity<List<Notification>> getByDestinataire(@PathVariable String destinataire) {
        List<Notification> notifications = notificationService.getByDestinataire(destinataire);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/destinataire/{destinataire}/unread")
    public ResponseEntity<List<Notification>> getUnread(@PathVariable String destinataire) {
        List<Notification> notifications = notificationService.getUnreadNotifications(destinataire);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/destinataire/{destinataire}/count-unread")
    public ResponseEntity<Integer> countUnread(@PathVariable String destinataire) {
        int count = notificationService.countUnread(destinataire);
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/destinataire/{destinataire}/read-all")
    public ResponseEntity<Void> markAllAsRead(@PathVariable String destinataire) {
        notificationService.markAllAsRead(destinataire);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}
package com.rental.controllers;

import com.rental.model.Notification;
import com.rental.model.enums.NotificationType;
import com.rental.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // Récupérer toutes les notifications
    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        List<Notification> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    // Récupérer les notifications par destinataire
    @GetMapping("/destinataire/{destinataire}")
    public ResponseEntity<List<Notification>> getNotificationsByDestinataire(@PathVariable String destinataire) {
        List<Notification> notifications = notificationService.getNotificationsByDestinataire(destinataire);
        return ResponseEntity.ok(notifications);
    }

    // Récupérer les notifications non lues par destinataire
    @GetMapping("/destinataire/{destinataire}/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@PathVariable String destinataire) {
        List<Notification> notifications = notificationService.getUnreadNotifications(destinataire);
        return ResponseEntity.ok(notifications);
    }

    // Récupérer les notifications par type
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Notification>> getNotificationsByType(@PathVariable NotificationType type) {
        List<Notification> notifications = notificationService.getNotificationsByType(type);
        return ResponseEntity.ok(notifications);
    }

    // Marquer une notification comme lue
    @PatchMapping("/{id}/read")
    public ResponseEntity<Notification> markAsRead(@PathVariable Long id) {
        Notification notification = notificationService.markAsRead(id);
        return ResponseEntity.ok(notification);
    }

    // Marquer toutes les notifications d'un destinataire comme lues
    @PatchMapping("/destinataire/{destinataire}/read-all")
    public ResponseEntity<Void> markAllAsRead(@PathVariable String destinataire) {
        notificationService.markAllAsRead(destinataire);
        return ResponseEntity.noContent().build();
    }

    // Compter les notifications non lues
    @GetMapping("/destinataire/{destinataire}/count-unread")
    public ResponseEntity<Long> countUnreadNotifications(@PathVariable String destinataire) {
        Long count = notificationService.countUnreadNotifications(destinataire);
        return ResponseEntity.ok(count);
    }

    // Supprimer une notification
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

    // Créer une notification de rappel client (pour WhatsApp)
    @PostMapping("/client/{clientId}/rappel")
    public ResponseEntity<Notification> createClientRappel(
            @PathVariable Long clientId,
            @RequestParam Long reservationId) {
        // On suppose que les services sont bien connectés
        // Cette méthode serait appelée par le service de réservation
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
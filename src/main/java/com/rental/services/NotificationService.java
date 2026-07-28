package com.rental.services;

import com.rental.model.Client;
import com.rental.model.Notification;
import com.rental.model.Reservation;
import com.rental.model.enums.NotificationType;
import com.rental.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    // Créer une notification admin pour la remise
    @Transactional
    public Notification createRemiseNotification(Client client, Reservation reservation) {
        String message = String.format(
                "Le client %s %s a effectué sa 6ème réservation (ID: %d). " +
                        "Il est éligible pour une remise !",
                client.getNom(),
                client.getPrenom(),
                reservation.getId()
        );

        Notification notification = new Notification();
        notification.setType(NotificationType.ADMIN_REMISE);
        notification.setDestinataire("admin");
        notification.setMessage(message);
        notification.setReservation(reservation);

        return notificationRepository.save(notification);
    }

    // Créer une notification admin pour le stock
    @Transactional
    public Notification createStockNotification(String type, Integer quantite) {
        String message = String.format(
                "⚠️ ALERTE STOCK : Le stock de '%s' est bas (%d unités restantes). " +
                        "Veuillez réapprovisionner.",
                type,
                quantite
        );

        Notification notification = new Notification();
        notification.setType(NotificationType.ADMIN_STOCK);
        notification.setDestinataire("admin");
        notification.setMessage(message);

        return notificationRepository.save(notification);
    }

    // Créer une notification client (rappel WhatsApp)
    @Transactional
    public Notification createClientRappelNotification(Client client, Reservation reservation) {
        String message = String.format(
                "🔔 RAPPEL : Le client %s %s a une réservation à venir (ID: %d) pour le %s.",
                client.getNom(),
                client.getPrenom(),
                reservation.getId(),
                reservation.getDateDebut()
        );

        Notification notification = new Notification();
        notification.setType(NotificationType.CLIENT_RAPPEL);
        notification.setDestinataire("client:" + client.getId());
        notification.setMessage(message);
        notification.setReservation(reservation);

        return notificationRepository.save(notification);
    }

    // Récupérer toutes les notifications
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    // Récupérer les notifications par destinataire
    public List<Notification> getNotificationsByDestinataire(String destinataire) {
        return notificationRepository.findByDestinataire(destinataire);
    }

    // Récupérer les notifications non lues par destinataire
    public List<Notification> getUnreadNotifications(String destinataire) {
        return notificationRepository.findByDestinataireAndEstLueFalse(destinataire);
    }

    // Marquer une notification comme lue
    @Transactional
    public Notification markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification non trouvée avec l'ID: " + id));

        notification.setEstLue(true);
        return notificationRepository.save(notification);
    }

    // Marquer toutes les notifications d'un destinataire comme lues
    @Transactional
    public void markAllAsRead(String destinataire) {
        List<Notification> notifications = notificationRepository.findByDestinataireAndEstLueFalse(destinataire);
        notifications.forEach(n -> n.setEstLue(true));
        notificationRepository.saveAll(notifications);
    }

    // Compter les notifications non lues
    public Long countUnreadNotifications(String destinataire) {
        return notificationRepository.findByDestinataireAndEstLueFalse(destinataire).stream().count();
    }

    // Supprimer une notification
    @Transactional
    public void deleteNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification non trouvée avec l'ID: " + id));
        notificationRepository.delete(notification);
    }

    // Récupérer les notifications par type
    public List<Notification> getNotificationsByType(NotificationType type) {
        return notificationRepository.findByType(type);
    }
}
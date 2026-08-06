// com/rental/services/NotificationService.java
package com.rental.services;

import com.rental.model.Client;
import com.rental.model.Notification;
import com.rental.model.Reservation;
import com.rental.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Transactional
    public Notification createNotification(Notification notification) {
        notification.setCreatedAt(LocalDateTime.now());
        notification.setEstLue(false);
        return notificationRepository.save(notification);
    }

    // ✅ Utiliser findAllOrderByDateDesc()
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAllOrderByDateDesc();
    }

    // ✅ Utiliser findByDestinataireOrderByDateDesc()
    public List<Notification> getByDestinataire(String destinataire) {
        return notificationRepository.findByDestinataireOrderByDateDesc(destinataire);
    }

    // ✅ Utiliser findUnreadByDestinataire()
    public List<Notification> getUnreadNotifications(String destinataire) {
        return notificationRepository.findUnreadByDestinataire(destinataire);
    }

    // ✅ Utiliser countUnreadByDestinataire()
    public int countUnread(String destinataire) {
        return notificationRepository.countUnreadByDestinataire(destinataire);
    }

    @Transactional
    public void markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification non trouvée avec l'ID: " + id));
        notification.setEstLue(true);
        notificationRepository.save(notification);
    }

    @Transactional
    public void markAllAsRead(String destinataire) {
        List<Notification> notifications = notificationRepository.findUnreadByDestinataire(destinataire);
        notifications.forEach(n -> n.setEstLue(true));
        notificationRepository.saveAll(notifications);
    }

    @Transactional
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    // ============================================
    // ✅ MÉTHODES DE CRÉATION DE NOTIFICATIONS
    // ============================================

    // Créer une notification de remise avec Client et Reservation
    public Notification createRemiseNotification(Client client, Reservation reservation) {
        Notification notification = new Notification();
        notification.setType("ADMIN_REMISE");
        notification.setDestinataire("admin");
        notification.setMessage("🎁 Remise disponible ! Le client " + client.getPrenom() + " " + client.getNom() +
                " a droit à une remise pour sa " + (client.getReservationCount()) + "ème réservation.");
        notification.setReservationId(reservation.getId());
        return createNotification(notification);
    }

    // Créer une notification de remise avec paramètres simples
    public Notification createRemiseNotification(String clientNom, String clientPrenom, Long reservationId) {
        Notification notification = new Notification();
        notification.setType("ADMIN_REMISE");
        notification.setDestinataire("admin");
        notification.setMessage("🎁 Remise disponible ! Le client " + clientPrenom + " " + clientNom +
                " a droit à une remise pour sa 6ème réservation.");
        notification.setReservationId(reservationId);
        return createNotification(notification);
    }

    // Créer une alerte stock
    public Notification createStockAlertNotification(String stockType, int quantite, int seuil) {
        Notification notification = new Notification();
        notification.setType("ADMIN_STOCK");
        notification.setDestinataire("admin");
        notification.setMessage("⚠️ Stock bas ! Le produit " + stockType +
                " n'a plus que " + quantite + " unités (seuil: " + seuil + ").");
        return createNotification(notification);
    }

    // Créer un rappel client
    public Notification createClientRappelNotification(Client client, Reservation reservation) {
        Notification notification = new Notification();
        notification.setType("CLIENT_RAPPEL");
        notification.setDestinataire("client:" + client.getId());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateFormatee = reservation.getDateDebut().format(formatter);

        notification.setMessage("📞 Rappel : Le client " + client.getPrenom() + " " + client.getNom() +
                " a une réservation prévue le " + dateFormatee + ". Pensez à le contacter.");
        notification.setReservationId(reservation.getId());
        return createNotification(notification);
    }

    // Créer un rappel client avec paramètres simples
    public Notification createClientRappelNotification(Long clientId, String clientNom, String clientPrenom, String date) {
        Notification notification = new Notification();
        notification.setType("CLIENT_RAPPEL");
        notification.setDestinataire("client:" + clientId);
        notification.setMessage("📞 Rappel : Le client " + clientPrenom + " " + clientNom +
                " a une réservation prévue le " + date + ". Pensez à le contacter.");
        return createNotification(notification);
    }
}
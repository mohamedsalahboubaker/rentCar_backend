package com.rental.repositories;

import com.rental.model.Notification;
import com.rental.model.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Notifications par destinataire
    List<Notification> findByDestinataire(String destinataire);

    // Notifications non lues par destinataire
    List<Notification> findByDestinataireAndEstLueFalse(String destinataire);

    // Notifications par type
    List<Notification> findByType(NotificationType type);

    // Notifications par réservation
    List<Notification> findByReservationId(Long reservationId);

    // Notifications non lues par type
    List<Notification> findByTypeAndEstLueFalse(NotificationType type);
}
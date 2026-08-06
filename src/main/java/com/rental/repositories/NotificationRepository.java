// com/rental/repositories/NotificationRepository.java
package com.rental.repositories;

import com.rental.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // ============================================
    // ✅ VERSION AVEC @Query (Recommandée)
    // ============================================

    @Query("SELECT n FROM Notification n ORDER BY n.createdAt DESC")
    List<Notification> findAllOrderByDateDesc();

    @Query("SELECT n FROM Notification n WHERE n.destinataire = :destinataire ORDER BY n.createdAt DESC")
    List<Notification> findByDestinataireOrderByDateDesc(@Param("destinataire") String destinataire);

    @Query("SELECT n FROM Notification n WHERE n.destinataire = :destinataire AND n.estLue = false ORDER BY n.createdAt DESC")
    List<Notification> findUnreadByDestinataire(@Param("destinataire") String destinataire);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.destinataire = :destinataire AND n.estLue = false")
    int countUnreadByDestinataire(@Param("destinataire") String destinataire);
}
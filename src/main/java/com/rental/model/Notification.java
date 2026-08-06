// com/rental/models/Notification.java
package com.rental.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false)
    private String type; // ADMIN_REMISE, ADMIN_STOCK, CLIENT_RAPPEL

    @Column(name = "destinataire", nullable = false)
    private String destinataire; // "admin" ou "client:{id}"

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "est_lue")
    private Boolean estLue = false;

    @Column(name = "reservation_id")
    private Long reservationId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
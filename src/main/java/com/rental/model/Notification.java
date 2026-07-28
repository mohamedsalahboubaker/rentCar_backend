package com.rental.model;

import com.rental.model.enums.NotificationType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(nullable = false, length = 50)
    private String destinataire;  // 'admin' ou 'client:{id}'

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "est_lue", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean estLue = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
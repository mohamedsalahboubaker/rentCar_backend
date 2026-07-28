package com.rental.model;

import com.rental.model.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voiture_id", nullable = false)
    private Voiture voiture;

    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_fin", nullable = false)
    private LocalDate dateFin;

    @Column(name = "nombre_jours", nullable = false)
    private Integer nombreJours;

    @Column(name = "prix_par_jour", nullable = false)
    private Double prixParJour;

    @Column(name = "prix_total", nullable = false)
    private Double prixTotal;

    @Column(nullable = false)
    private Double avance = 0.0;

    @Column(name = "caution_payee", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean cautionPayee = false;

    @Column(name = "montant_caution")
    private Double montantCaution;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ReservationStatus status = ReservationStatus.EN_ATTENTE;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
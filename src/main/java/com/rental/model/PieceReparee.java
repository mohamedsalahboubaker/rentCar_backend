package com.rental.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "piece_reparee")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PieceReparee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voiture_id", nullable = false)
    private Voiture voiture;

    @Column(name = "nom_piece", nullable = false, length = 200)
    private String nomPiece;

    @Column(nullable = false)
    private Double prix;

    @Column(name = "date_reparation", nullable = false)
    private LocalDate dateReparation;

    @Column(columnDefinition = "TEXT")
    private String notes;

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